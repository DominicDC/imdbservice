package com.dc.rest.imdbservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/***
 ** Author: Dominic Coutinho
 ** Description: This pojo defines the link between the titles and its ratings
 */

@Entity
@Table(name = "title_ratings", schema = "imdb")
public class Ratings {

    @Id
    @Column(name = "tr_title_id")
    private String titleId;

    @Column(name = "tr_average_rating")
    private Double avgRating;
    
    @Column(name = "tr_num_votes")
    private Integer noOfVotes;

    public Ratings(){
	
    }
    
    
    /**
     * @param titleId
     * @param avgRating
     * @param noOfVotes
     */
    public Ratings(String titleId, Double avgRating, Integer noOfVotes) {
	super();
	this.titleId = titleId;
	this.avgRating = avgRating;
	this.noOfVotes = noOfVotes;
    }


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

    @Override
    public String toString() {
	return "Ratings [titleId=" + titleId + ", avgRating=" + avgRating + ", noOfVotes=" + noOfVotes + "]";
    }
    
    
}
