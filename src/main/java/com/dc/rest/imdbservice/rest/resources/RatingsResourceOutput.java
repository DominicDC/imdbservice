package com.dc.rest.imdbservice.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;
/***
 ** Author: Dominic Coutinho
 ** Description: DTO for rating resource output
 */
public class RatingsResourceOutput extends RepresentationModel<RatingsResourceOutput> {
 
    @JsonProperty("id")
    private String titleId;
    private Double avgRating;
    private Integer noOfVotes;
    
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
    public Integer getNoOfVotes() {
        return this.noOfVotes;
    }
    public void setNoOfVotes(Integer noOfVotes) {
        this.noOfVotes = noOfVotes;
    }
}
