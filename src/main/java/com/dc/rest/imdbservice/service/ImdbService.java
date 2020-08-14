package com.dc.rest.imdbservice.service;

import com.dc.rest.imdbservice.entity.Ratings;
import com.dc.rest.imdbservice.exception.FileParsingException;
import com.dc.rest.imdbservice.exception.ImdbServiceException;
import com.dc.rest.imdbservice.exception.TitleNotFoundException;
/***
 ** Author: Dominic Coutinho
 ** Description: Core IMDB logic service skeleton
 */
public interface ImdbService {
    void downloadData(String imdbUrl) throws ImdbServiceException;
    void loadFeeds() throws FileParsingException;
    void recomputeRatings();
    Ratings reviseRatingsForEpisode(String titleId, Double revisedRating) throws TitleNotFoundException;
}
