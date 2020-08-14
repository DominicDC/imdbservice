package com.dc.rest.imdbservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/***
 ** Author: Dominic Coutinho
 ** Description: This pojo defines the basic properties associated with a title
 */

@Entity
@Table(name = "title_basics", schema = "imdb")
public class TitleBasics {

    @Id
    @Column(name = "tb_title_id")
    private String id;

    @Column(name = "tb_title_type")
    private String type;

    @Column(name = "tb_primary_title")
    private String primaryTitle;

    @Column(name = "tb_original_title")
    private String originalTitle;

    @Column(name = "tb_is_adult")
    private Integer isAdult;

    @Column(name = "tb_start_year")
    private Integer startYear;

    @Column(name = "tb_end_year")
    private Integer endYear;

    @Column(name = "tb_runtime_minutes")
    private String runTimeMinutes;

    @Column(name = "tb_genres")
    private String genres;

    public String getId() {
	return this.id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getType() {
	return this.type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getPrimaryTitle() {
	return this.primaryTitle;
    }

    public void setPrimaryTitle(String primaryTitle) {
	this.primaryTitle = primaryTitle;
    }

    public String getOriginalTitle() {
	return this.originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
	this.originalTitle = originalTitle;
    }

    public Integer getIsAdult() {
	return this.isAdult;
    }

    public void setIsAdult(Integer isAdult) {
	this.isAdult = isAdult;
    }

    public Integer getStartYear() {
	return this.startYear;
    }

    public void setStartYear(Integer startYear) {
	this.startYear = startYear;
    }

    public Integer getEndYear() {
	return this.endYear;
    }

    public void setEndYear(Integer endYear) {
	this.endYear = endYear;
    }

    public String getRunTimeMinutes() {
	return this.runTimeMinutes;
    }

    public void setRunTimeMinutes(String runTimeMinutes) {
	this.runTimeMinutes = runTimeMinutes;
    }

    public String getGenres() {
	return this.genres;
    }

    public void setGenres(String genres) {
	this.genres = genres;
    }

    @Override
    public String toString() {
	return "TitleBasics [id=" + id + ", type=" + type + ", primaryTitle=" + primaryTitle + ", originalTitle="
		+ originalTitle + ", isAdult=" + isAdult + ", startYear=" + startYear + ", endYear=" + endYear
		+ ", runTimeMinutes=" + runTimeMinutes + ", genres=" + genres + "]";
    }

    
}
