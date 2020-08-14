package com.dc.rest.imdbservice.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

/***
 ** Author: Dominic Coutinho
 ** Description: DTO for rating resource input
 */
public class RatingsResourceInput extends RepresentationModel<RatingsResourceInput> {

    @JsonProperty("id")
    private String titleId;
    private Double avgRating;

    public String getTitleId() {
	return this.titleId;
    }

    public void setTitleId(String titleId) {
	this.titleId = titleId;
    }

    public Double getAvgRating() {
	return this.avgRating;
    }

    public void setAvgRating(Double avgRating) {
	this.avgRating = avgRating;
    }

}
