package com.dc.rest.imdbservice.rest.controller;

import com.dc.rest.imdbservice.entity.Ratings;
import com.dc.rest.imdbservice.exception.ErrorMessage;
import com.dc.rest.imdbservice.exception.ImdbServiceException;
import com.dc.rest.imdbservice.exception.TitleNotFoundException;
import com.dc.rest.imdbservice.rest.assembler.RatingsResourceAssembler;
import com.dc.rest.imdbservice.rest.resources.RatingsResourceInput;
import com.dc.rest.imdbservice.rest.resources.RatingsResourceOutput;
import com.dc.rest.imdbservice.service.ImdbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/***
 ** Author: Dominic Coutinho
 ** Description: Single controller to initiate the feed download, processing and rating updates
 */
@RestController
@RequestMapping(value = "/v1/imdb", produces = { MediaTypes.HAL_JSON_VALUE })
public class ImdbController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImdbController.class);

    @Autowired
    private ImdbService imdbService;

    @Autowired
    private TaskExecutor executor;

    @GetMapping(value = "/download")
    public HttpEntity<?> downloadFeeds() throws ImdbServiceException {
	String imdbUrl = "https://datasets.imdbws.com/";
	executor.execute(new Runnable() {
	    public void run() {
		try {
		    imdbService.downloadData(imdbUrl);
		} catch (Exception e) {
			e.printStackTrace();
		    LOGGER.error(ErrorMessage.INGESTION_ERROR);
		}
	    }
	});
	return new ResponseEntity<>(HttpStatus.ACCEPTED);

    }

    @PutMapping(value = "/update")
    public HttpEntity<RatingsResourceOutput> updateTitleRating(@RequestBody RatingsResourceInput ratingsResourceInput) throws TitleNotFoundException {
	Ratings rating = imdbService.reviseRatingsForEpisode(ratingsResourceInput.getTitleId(),
		ratingsResourceInput.getAvgRating());
	RatingsResourceAssembler assembler = new RatingsResourceAssembler();
	return new ResponseEntity<RatingsResourceOutput>(assembler.toModel(rating), HttpStatus.OK);
    }

}
