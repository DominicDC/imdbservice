package com.dc.rest.imdbservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/***
 ** Author: Dominic Coutinho
 ** Description: This pojo defines the relationship of titles-seasons-episodes
 */

@Entity
@Table(name = "title_episodes", schema = "imdb")
public class Episodes {
    
    @Id
    @Column(name = "te_title_id")
    private String titleId;

    @Column(name = "te_parent_title_id")
    private String parentTitleId;
    
    @Column(name = "te_season_number")
    private Integer seasonNumber;
    
    @Column(name = "te_episode_number")
    private Integer episodeNumber;

    public String getTitleId() {
        return this.titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public String getParentTitleId() {
        return this.parentTitleId;
    }

    public void setParentTitleId(String parentTitleId) {
        this.parentTitleId = parentTitleId;
    }

    public Integer getSeasonNumber() {
        return this.seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public Integer getEpisodeNumber() {
        return this.episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    @Override
    public String toString() {
	return "Episodes [titleId=" + titleId + ", parentTitleId=" + parentTitleId + ", seasonNumber=" + seasonNumber
		+ ", episodeNumber=" + episodeNumber + "]";
    } 
   
}
