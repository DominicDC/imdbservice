package com.dc.rest.imdbservice.service;

import com.dc.rest.imdbservice.constants.Feeds;
import com.dc.rest.imdbservice.constants.TitleTypes;
import com.dc.rest.imdbservice.entity.*;
import com.dc.rest.imdbservice.exception.ErrorMessage;
import com.dc.rest.imdbservice.exception.FileParsingException;
import com.dc.rest.imdbservice.exception.ImdbServiceException;
import com.dc.rest.imdbservice.exception.TitleNotFoundException;
import com.dc.rest.imdbservice.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/***
 ** Author: Dominic Coutinho
 ** Description: This class reads feed files hosted at "../feeds" path and loads it into the DB
 *
 * Usecase:
 * 1. Load all the feed files into DB
 * 2. Recomputes series rating based on episodes of a series
 * 2. Expose a service to update the ratings of a title launched in 2017 only
 *
 * Key Methods:
 * 1. downloadData - Downloads the feed files form IMDB
 * 2. loadFeeds - loads data into DB
 * 3. recomputeRatings - Logic to recompute series ratings
 *
 * NOTE:
 * Currently the code facilitates updates to titles launched in the year 2017 at load
 * Few improvements to reload the cache on server restart will be done on need basis
 *
 *
 */

@Service
public class ImdbServiceImpl implements ImdbService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImdbServiceImpl.class);

    private static final String filePath = Paths.get(".", "feeds").normalize().toAbsolutePath().toString()
	    + File.separator;

    @Autowired
    private DownloadService downloadService;

    @Autowired
    private TitleBasicsRepository titleBasicsRepository;

    @Autowired
    private TitleExtensionRepository titleExtensionRepository;

    @Autowired
    private EpisodesRepository episodesRepository;

    @Autowired
    private RatingsRepository ratingsRepository;

    @Autowired
    private PrimaryCastRepository primaryCastRepository;

    @Autowired
    private CastDetailsRepository castDetailsRepository;

    @Autowired
    private CastCrewRepository castCrewRepository;

    @Autowired
    private CastDetailsBatchUpdateRepository castDetailsBatchUpdateRepository;

    @Autowired
    private PrimaryCastBatchRepository primaryCastBatchRepository;

    @Autowired
    private DataSource dataSource;

    private Map<String, String> episodeParentMap;

    private Map<String, String> titleMap;

    private Map<String, List<Ratings>> ratingsMap;

    private Set<String> castIds;

    private List<String> titleTypes = Arrays.asList(TitleTypes.MOVIES, TitleTypes.TV_SERIES, TitleTypes.TV_EPISODE,
	    TitleTypes.TV_MINI_SERIES, TitleTypes.TV_MOVIE);

    private static final Integer BATCH_SIZE = 50000;
	private static final Integer FILTER_YEAR = 2017;

    public ImdbServiceImpl() {
	episodeParentMap = new HashMap<>();
	titleMap = new HashMap<>();
	ratingsMap = new HashMap<>();
    }

    public void downloadData(String url) throws ImdbServiceException {

	downloadService.downloadData(url);
	loadFeeds();
	LOGGER.info("*******Data successfully persisted and re-computed*******");
    }

    /**************** Load Feeds ********************/
    public void loadFeeds() throws FileParsingException {

	// Clean up before loading data
	cleanupData();

	LOGGER.info("Start loading feeds");
	loadTitleBasicFile();
	loadTitleEpisodeFile();
	loadTitleRatingFile();
	loadTitleExtensionFeeds();
	loadTitleCrewFile();
	loadTitlePrimaryCastFile();
	//loadTitleCastDetailsFile();

	LOGGER.info("Completed loading feeds");
	//computeRatingsForSeriesWithEpisode("tt6972580", 1.0);

	LOGGER.info("Start rating computation process");
	recomputeRatings();
	LOGGER.info("End rating computation process");
    }

    public void loadTitleBasicFile() throws FileParsingException {

	LOGGER.info("Start Data load of " + Feeds.TITLE_BASICS_TSV + " feed");
	Path path = Paths.get(filePath + Feeds.TITLE_BASICS_TSV);
	List<TitleBasics> titlesList = new ArrayList<>();
	try (Stream<String> lines = Files.lines(path)) {
	    titlesList = lines.skip(1).map(mapToTitles)
		    .filter(title -> title.getStartYear() != null && FILTER_YEAR.equals(title.getStartYear())
			    && title.getType() != null && titleTypes.contains(title.getType()))
		    .collect(Collectors.toList());
	} catch (IOException e) {
	    throw new FileParsingException(ErrorMessage.PARSING_ERROR, e);
	}
	titleBasicsRepository.saveAll(titlesList);
	LOGGER.info("Completed Data load of " + Feeds.TITLE_BASICS_TSV + " feed . Record count : " + titlesList.size());
	titlesList.clear();
	LOGGER.info("TitleMap :" + titleMap.size());
    }

    public void loadTitleEpisodeFile() throws FileParsingException {
	Path path = Paths.get(filePath + Feeds.TITLE_EPISODE_TSV);

	LOGGER.info("Start Data load of " + Feeds.TITLE_EPISODE_TSV + " feed");
	List<Episodes> episodes = new ArrayList<>();
	try (Stream<String> lines = Files.lines(path)) {
	    episodes = lines.skip(1).map(mapToEpisodes).filter(episode -> titleMap.containsKey(episode.getTitleId()))
		    .collect(Collectors.toList());
	} catch (IOException e) {
	    throw new FileParsingException(ErrorMessage.PARSING_ERROR, e);
	}
	episodesRepository.saveAll(episodes);
	LOGGER.info("Completed Data load of " + Feeds.TITLE_EPISODE_TSV + " feed . Record count : " + episodes.size());
	episodes.clear();
	LOGGER.info("EpisodeParentMap : " + episodeParentMap.size());
    }

    public void loadTitleRatingFile() throws FileParsingException {
	LOGGER.info("Start Data load of " + Feeds.TITLE_RATINGS_TSV + " feed");
	Path path = Paths.get(filePath + Feeds.TITLE_RATINGS_TSV);
	List<Ratings> ratings = new ArrayList<>();
	try (Stream<String> lines = Files.lines(path)) {
	    ratings = lines.skip(1).map(mapToRatings).filter(rating -> titleMap.containsKey(rating.getTitleId()))
		    .collect(Collectors.toList());
	} catch (IOException e) {
	    throw new FileParsingException(ErrorMessage.PARSING_ERROR, e);
	}
	ratingsRepository.saveAll(ratings);
	LOGGER.info("Completed Data load of " + Feeds.TITLE_RATINGS_TSV + " feed . Record count : " + ratings.size());
	ratings.clear();
	LOGGER.info("RatingsMap : " + ratingsMap.size());
    }

    public void loadTitleCrewFile() throws FileParsingException {
	LOGGER.info("Start Data load of " + Feeds.TITLE_CREW_TSV + " feed");
	Path path = Paths.get(filePath + Feeds.TITLE_CREW_TSV);
	List<CastCrew> castCrew = new ArrayList<>();
	try (Stream<String> lines = Files.lines(path)) {
	    castCrew = lines.skip(1).map(mapToCastCrew).filter(crew -> titleMap.containsKey(crew.getTitleid()))
		    .collect(Collectors.toList());
	} catch (IOException e) {
	    throw new FileParsingException(ErrorMessage.PARSING_ERROR, e);
	}
	castCrewRepository.saveAll(castCrew);
	LOGGER.info("Completed Data load of " + Feeds.TITLE_CREW_TSV + " feed . Record count : " + castCrew.size());
	castCrew.clear();
    }

    public void loadTitleExtensionFeeds() throws FileParsingException {
	LOGGER.info("Start Data load of " + Feeds.TITLE_AKAS_TSV + " feed");
	Path path = Paths.get(filePath + Feeds.TITLE_AKAS_TSV);
	List<TitleExtension> titleExtensions = new ArrayList<>();
	try (Stream<String> lines = Files.lines(path)) {
	    titleExtensions = lines.skip(1).map(mapToTitleExtension)
		    .filter(titleExtension -> titleMap.containsKey(titleExtension.getTitleId()))
		    .collect(Collectors.toList());
	} catch (IOException e) {
	    throw new FileParsingException(ErrorMessage.PARSING_ERROR, e);
	}
	titleExtensionRepository.saveAll(titleExtensions);
	LOGGER.info(
		"Completed Data load of " + Feeds.TITLE_AKAS_TSV + " feed . Record count : " + titleExtensions.size());
	titleExtensions.clear();
    }

    public void loadTitlePrimaryCastFile() throws FileParsingException {
	LOGGER.info("Start Data load of " + Feeds.TITLE_PRINCIPALS_TSV + " feed");
	Path path = Paths.get(filePath + Feeds.TITLE_PRINCIPALS_TSV);
	List<PrimaryCast> primaryCast = new ArrayList<>();
	try (Stream<String> lines = Files.lines(path)) {
	    primaryCast = lines.skip(1).map(mapToPrimaryCast)
		    .filter(rating -> titleMap.containsKey(rating.getTitleId())).collect(Collectors.toList());
	} catch (IOException e) {
	    LOGGER.error("Error Occurred: " + e);
	    throw new FileParsingException(ErrorMessage.PARSING_ERROR, e);
	}
	castIds = primaryCast.stream().map(cast -> cast.getCastId()).collect(Collectors.toSet());
	LOGGER.info("castIds"+castIds.size());
	saveCastDetailsRecords(primaryCast);
	// primaryCastBatchRepository.bulkSave(primaryCast);
	LOGGER.info("Completed Data load of " + Feeds.TITLE_PRINCIPALS_TSV + " feed . Record count : "
		+ primaryCast.size());
	primaryCast.clear();
    }

    public void loadTitleCastDetailsFile() throws FileParsingException {
	int count = 0;
	LOGGER.info("Start Data load of " + Feeds.NAME_BASICS_TSV + " feed");
	Path path = Paths.get(filePath + Feeds.NAME_BASICS_TSV);
	List<CastDetails> castDetails = new ArrayList<>();
	try (Stream<String> lines = Files.lines(path)) {
	    castDetails = lines.skip(1).map(mapToCastDetails)
		    //.filter(x -> castIds.contains(x.getCastId()))
		    .collect(Collectors.toList());
	} catch (IOException e) {
	    throw new FileParsingException(ErrorMessage.PARSING_ERROR, e);
	}
	castDetailsBatchUpdateRepository.bulkSave(castDetails);
	LOGGER.info("Completed Data load of " + Feeds.NAME_BASICS_TSV + " feed . Record count : " + castDetails.size());
	castDetails.clear();
    }

    public void saveTitleCastRecords(List<CastDetails> entities) {

	try {
	    final List<CastDetails> savedEntities = new ArrayList<>();
	    int count = 0;
	    int iterations = 0;
	    for (CastDetails cast : entities) {
		savedEntities.add(cast);
		count++;
		if (count % BATCH_SIZE == 0) {
		    count++;
		    LOGGER.info("Flushing the title cast records" + count);
		    iterations++;
		    castDetailsRepository.saveAll(entities);
		    castDetailsRepository.flush();
		}

	    }
	    LOGGER.info("Saved primary cast data in " + iterations + "iterations");
	    savedEntities.clear();
	} catch (Exception e) {
	    e.printStackTrace();
	    LOGGER.error("Error Occurred" + e);
	}
    }

    public void saveCastDetailsRecords(List<PrimaryCast> entities) {

	try {
	    final List<PrimaryCast> savedEntities = new ArrayList<>(entities.size());
	    int count = 0;
	    int iterations = 0;
	    for (PrimaryCast cast : entities) {
		savedEntities.add(cast);
		count++;
		if (count % BATCH_SIZE == 0) {
		    count++;
		    LOGGER.info("Flush a batch of inserts and release memory." + savedEntities.size());
		    // Flush a batch of inserts and release memory.
		    iterations++;
		    primaryCastRepository.saveAll(savedEntities);
		    primaryCastRepository.flush();
		    savedEntities.clear();
		}

	    }
	    LOGGER.info("Saved primary cast data in " + iterations + "iterations");
	} catch (Exception e) {
	    e.printStackTrace();
	    LOGGER.error("Error occurred" + e);
	}

    }

    /**************** Mapping Functions ********************/

    private Function<String, TitleBasics> mapToTitles = (line) -> {
	String[] content = line.trim().split("\t");
	TitleBasics title = new TitleBasics();
	if (content.length == 9) {
	    title.setId(content[0]);
	    title.setType(content[1]);
	    title.setPrimaryTitle(content[2]);
	    title.setOriginalTitle(content[3]);
	    title.setIsAdult(getValidInteger(content[4]));
	    title.setStartYear(getValidInteger(content[5]));
	    title.setEndYear((getValidInteger(content[6])));
	    title.setRunTimeMinutes(content[7]);
	    title.setGenres(content[8]);
	}

	if (title != null && title.getStartYear() != null && FILTER_YEAR.equals(title.getStartYear())) {
	    titleMap.put(title.getId(), title.getType());
	}
	return title;
    };

    private Function<String, Ratings> mapToRatings = (line) -> {
	String[] content = line.trim().split("\t");
	String titleId = content[0];
	Ratings ratings = new Ratings();
	ratings.setTitleId(titleId);
	ratings.setAvgRating(Double.parseDouble(content[1]));
	ratings.setNoOfVotes(Integer.parseInt(content[2]));

	String parent = episodeParentMap.get(titleId);
	if (parent != null && (TitleTypes.TV_SERIES.equals(titleMap.get(parent))
		|| TitleTypes.TV_MINI_SERIES.equals(titleMap.get(parent)))) {
	    if (ratingsMap.containsKey(parent)) {
		List<Ratings> ratingList = ratingsMap.get(parent);
		ratingList.add(ratings);
	    } else {
		List<Ratings> ratingList = new ArrayList<>();
		ratingList.add(ratings);
		ratingsMap.put(parent, ratingList);
	    }
	}
	return ratings;
    };

    private Function<String, CastDetails> mapToCastDetails = (line) -> {
	String[] content = line.trim().split("\t");
	CastDetails castDetails = new CastDetails();
	if (content.length == 6) {
	    castDetails.setCastId(content[0]);
	    castDetails.setPrimaryName(content[1]);
	    castDetails.setBirthYear(getValidInteger(content[2]));
	    castDetails.setDeathYear(getValidInteger(content[3]));
	    castDetails.setProfession(content[4]);
	    castDetails.setKnownForTitles(content[5]);
	}
	return castDetails;
    };

    private Function<String, PrimaryCast> mapToPrimaryCast = (line) -> {
	String[] content = line.trim().split("\t");
	PrimaryCast primaryCast = new PrimaryCast();
	if (content.length == 6) {
	    primaryCast.setTitleId(content[0]);
	    primaryCast.setOrdering(getValidInteger(content[1]));
	    primaryCast.setCastId(content[2]);
	    primaryCast.setCategoryOfJob(content[3]);
	    primaryCast.setJob(content[4]);
	    primaryCast.setCharacter(content[5]);
	}
	return primaryCast;
    };

    private Function<String, TitleExtension> mapToTitleExtension = (line) -> {
	String[] content = line.trim().split("\t");
	TitleExtension titleExt = new TitleExtension();
	if (content.length == 8) {
	    titleExt.setId(UUID.randomUUID().toString());
	    titleExt.setTitleId(content[0]);
	    titleExt.setOrdering(getValidInteger(content[1]));
	    titleExt.setLocalizedTitle(content[2]);
	    titleExt.setRegion(content[3]);
	    titleExt.setLanguage(content[4]);
	    titleExt.setType(content[5]);
	    titleExt.setAttributes(content[6]);
	    titleExt.setIsOriginalTitle(getValidInteger(content[7]));
	}
	return titleExt;
    };

    private Function<String, Episodes> mapToEpisodes = (line) -> {
	String[] content = line.trim().split("\t");

	Episodes episodes = new Episodes();
	if (content.length == 4) {
	    String episodeId = content[0];
	    String parentId = content[1];
	    episodes.setTitleId(episodeId);
	    episodes.setParentTitleId(parentId);
	    episodes.setSeasonNumber(getValidInteger(content[2]));
	    episodes.setEpisodeNumber(getValidInteger(content[3]));

	    if (titleMap.containsKey(episodeId)) {
		String type = titleMap.get(episodeId);
		if (TitleTypes.TV_EPISODE.equals(type))
		    episodeParentMap.put(episodeId, parentId);
	    }
	}
	return episodes;
    };

    private Function<String, CastCrew> mapToCastCrew = (line) -> {
	String[] content = line.trim().split("\t");
	CastCrew castCrew = new CastCrew();
	if (content.length == 3) {
	    castCrew.setId(UUID.randomUUID().toString());
	    castCrew.setTitleid(content[0]);
	    castCrew.setDirectorId(content[1]);
	    castCrew.setWriterId(content[2]);
	}
	return castCrew;
    };

    public Integer getValidInteger(String no) {
	return ("\\N".equals(no)) ? null : Integer.parseInt(no);

    }

    public void cleanupData() {
	LOGGER.info("Start cleanup prior to loading feeds");
	ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
	populator.setScripts(new Resource[] { new ClassPathResource("db_cleanup.sql") });
	DatabasePopulatorUtils.execute(populator, dataSource);
	LOGGER.info("End cleanup");
    }

    public void recomputeRatings() {
	List<Ratings> seriesRatings = new ArrayList<>();
	LOGGER.info("ratingsMap size" + ratingsMap.size());
	for (Entry<String, List<Ratings>> entry : ratingsMap.entrySet()) {
	    String seriesId = entry.getKey();
	    // Compute the ratings for the series based on the episodes
	    Double revisedRating = computeRatingsForSeries(entry.getValue());
	    LOGGER.info("Revised Rating for series : " + seriesId + " = " + revisedRating);
	    if (revisedRating != null) {
		// Update the rating
		Ratings seriesRating = ratingsRepository.findByTitleId(seriesId);
		if (seriesRating != null) {
		    seriesRating.setAvgRating(revisedRating);
		    seriesRatings.add(seriesRating);
		}

	    }
	}
	LOGGER.info("Updating records for no of series :" + seriesRatings.size());
	ratingsRepository.saveAll(seriesRatings);
    }

    /**
     * Rating of series = avg of episodes ratings
     * 
     * @param ratings
     * @return
     */
    public Double computeRatingsForSeries(List<Ratings> ratings) {
	Double revisedRating = null;
	if (ratings != null && ratings.size() > 0) {
	    double sum = ratings.stream().map(Ratings::getAvgRating).mapToDouble(Double::doubleValue).sum();
	    revisedRating = sum / ratings.size();
	}
	return revisedRating;
    }

    public Double computeRatingsForSeriesWithEpisode(String titleId, Double revisedRating) {
	LOGGER.info("Computing revised rating for series with episode : " + titleId);
	String seriesId = episodeParentMap.get(titleId);
	if (seriesId != null) {
	    List<Ratings> episodeRatings = ratingsMap.get(seriesId);
	    System.out.println("Size of episodeRatings" + episodeRatings);
	    Ratings oldEpisode = episodeRatings.stream().filter(episode -> titleId.equals(episode.getTitleId()))
		    .findAny().get();
	    oldEpisode.setAvgRating(revisedRating);
	    return computeRatingsForSeries(episodeRatings);
	}
	return null;
    }

    public Ratings reviseRatingsForEpisode(String titleId, Double revisedRating) throws TitleNotFoundException {

	if (titleMap != null && !titleMap.containsKey(titleId)) {
	    throw new TitleNotFoundException(ErrorMessage.TITLE_NOT_FOUND);
	}
	LOGGER.info("Computing revised rating for episode : " + titleId);

	// Update the rating for episode
	Ratings rating = updateRatingInDb(titleId, revisedRating);
	String seriesId = episodeParentMap.get(titleId);

	// If episode belong to series , recompute series rating as well and
	// save
	if (seriesId != null) {
	    revisedRating = computeRatingsForSeriesWithEpisode(titleId, revisedRating);
	    Ratings seriesRating = updateRatingInDb(seriesId, revisedRating);
	    LOGGER.info("Series rating recalculated as :" + seriesRating);
	}
	return rating;
    }

    public Ratings updateRatingInDb(String titleId, Double revisedRating) {
	Ratings rating = null;
	if (revisedRating != null) {
	    // Update the rating
	    rating = ratingsRepository.findByTitleId(titleId);
	    if (rating != null) {
		rating.setAvgRating(revisedRating);
		ratingsRepository.save(rating);
	    }
	}
	return rating;
    }

    public void setEpisodeParentMap(Map<String, String> episodeParentMap) {
	this.episodeParentMap = episodeParentMap;
    }

    public void setTitleMap(Map<String, String> titleMap) {
	this.titleMap = titleMap;
    }

    public void setRatingsMap(Map<String, List<Ratings>> ratingsMap) {
	this.ratingsMap = ratingsMap;
    }

}
