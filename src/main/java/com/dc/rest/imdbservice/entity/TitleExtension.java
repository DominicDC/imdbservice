package com.dc.rest.imdbservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/***
 ** Author: Dominic Coutinho
 ** Description: This pojo is an extension of the title and is responsible for additional title information
 */

@Entity
@Table(name = "title_akas", schema = "imdb" /*,uniqueConstraints={@UniqueConstraint(columnNames={"ta_titleId","ta_ordering"})}*/)
public class TitleExtension {
    
    @Id
    @Column(name = "ta_id")
    private String id;
    
    @Column(name = "ta_title_id")
    private String titleId;

    @Column(name = "ta_ordering")
    private Integer ordering;

    @Column(name = "ta_title")
    private String localizedTitle;

    @Column(name = "ta_region")
    private String region;

    @Column(name = "ta_language")
    private String language;

    @Column(name = "ta_types")
    private String type;

    @Column(name = "ta_attributes")
    private String attributes;

    @Column(name = "ta_is_original_title")
    private Integer isOriginalTitle;

    public String getTitleId() {
        return this.titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public Integer getOrdering() {
        return this.ordering;
    }

    public void setOrdering(Integer ordering) {
        this.ordering = ordering;
    }

    public String getLocalizedTitle() {
        return this.localizedTitle;
    }

    public void setLocalizedTitle(String localizedTitle) {
        this.localizedTitle = localizedTitle;
    }

    public String getRegion() {
        return this.region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAttributes() {
        return this.attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public Integer getIsOriginalTitle() {
        return this.isOriginalTitle;
    }
    
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIsOriginalTitle(Integer isOriginalTitle) {
        this.isOriginalTitle = isOriginalTitle;
    }
    
}
