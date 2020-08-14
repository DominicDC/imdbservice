package com.dc.rest.imdbservice.entity;

import javax.persistence.*;
import java.util.UUID;


/***
 ** Author: Dominic Coutinho
 ** Description: This pojo defines the properties of primary casts linked with a title
 */

@Entity
@Table(name = "title_principals", schema = "imdb" ,uniqueConstraints={@UniqueConstraint(columnNames={"tp_title_id","tp_cast_id","tp_ordering"})})
public class PrimaryCast {

    @Id
    @Column(name = "tp_id")
    private String id;
    
    @Column(name = "tp_title_id")
    private String titleId;

    @Column(name = "tp_ordering")
    private Integer ordering;
    
    @Column(name = "tp_cast_id")
    private String castId;
    
    @Column(name = "tp_category")
    private String categoryOfJob;

    @Column(name = "tp_job")
    private String job;

    @Column(name = "tp_characters", columnDefinition = "TEXT")
    private String character;

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

    public String getCastId() {
        return this.castId;
    }

    public void setCastId(String castId) {
        this.castId = castId;
    }

    public String getCategoryOfJob() {
        return this.categoryOfJob;
    }

    public void setCategoryOfJob(String categoryOfJob) {
        this.categoryOfJob = categoryOfJob;
    }

    public String getJob() {
        return this.job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getCharacter() {
        return this.character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    @PrePersist
    void onCreate() {
      this.setId(UUID.randomUUID().toString());
    }
    

}
