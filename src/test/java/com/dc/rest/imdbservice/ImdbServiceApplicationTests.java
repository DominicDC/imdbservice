package com.dc.rest.imdbservice;

import com.dc.rest.imdbservice.constants.TitleTypes;
import com.dc.rest.imdbservice.entity.Ratings;
import com.dc.rest.imdbservice.exception.TitleNotFoundException;
import com.dc.rest.imdbservice.repository.RatingsRepository;
import com.dc.rest.imdbservice.service.ImdbServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ImdbServiceApplicationTests {


	@Autowired
	@InjectMocks
	private ImdbServiceImpl ImdbService;

	@Mock
	private RatingsRepository ratingsRepository;

	private Map<String, String> episodeParentMap = new HashMap<>();

	private Map<String, String> titleMap = new HashMap<>();

	private Map<String, List<Ratings>> ratingsMap = new HashMap<>();

	@Test
	public void contextLoads() {
	}

	@BeforeEach
	public void setup() {
		List<Ratings> ratings = new ArrayList<>();
		ratings.add(new Ratings("tt6831916", 7.9, 159));
		ratings.add(new Ratings("tt6854022", 8.4, 129));
		ratings.add(new Ratings("tt6899062", 8.0, 136));
		ratings.add(new Ratings("tt6957686", 8.3, 96));
		ratings.add(new Ratings("tt6972574", 7.9, 186));
		ratings.add(new Ratings("tt6972580", 8.0, 139));
		ratings.add(new Ratings("tt6972586", 8.1, 114));
		ratings.add(new Ratings("tt6972592", 8.2, 92));

		// Series data
		titleMap.put("tt1210820", TitleTypes.TV_SERIES);
		ratingsMap.put("tt1210820", ratings);

		// Episode data
		episodeParentMap.put("tt6899062", "tt1210820");
		titleMap.put("tt6899062", TitleTypes.TV_EPISODE);

		ImdbService.setEpisodeParentMap(episodeParentMap);
		ImdbService.setTitleMap(titleMap);
		ImdbService.setRatingsMap(ratingsMap);

	}

	@Test
	public void testReviseRatingsForEpisodeInvalid() {
		Exception exp = assertThrows(TitleNotFoundException.class,
				() -> ImdbService.reviseRatingsForEpisode("InvalidTitle", 1.5));
	}

	@Test
	public void testReviseRatingsForEpisodeWithNoParent() throws TitleNotFoundException{
		when(ratingsRepository.findByTitleId("tt1210820")).thenReturn(new Ratings("tt1210820", 8.5, 192));
		Ratings rating = ImdbService.reviseRatingsForEpisode("tt1210820", 1.5);
		assertEquals(1.5, rating.getAvgRating(), 0.0);
	}

	@Test
	public void testReviseRatingsForEpisode() throws TitleNotFoundException {
		when(ratingsRepository.findByTitleId("tt6899062")).thenReturn(new Ratings("tt6972592", 8.2, 92));
		Ratings rating = ImdbService.reviseRatingsForEpisode("tt6899062", 1.5);
		assertEquals(1.5, rating.getAvgRating(), 0.0);
	}

}
