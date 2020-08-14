package com.dc.rest.imdbservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/***
 ** Author: Dominic Coutinho
 ** Description: This pojo links the titles with the crew
 */

@Entity
@Table(name = "title_crew", schema = "imdb")
public class CastCrew {

    @Id
    @Column(name = "tc_id")
    private String id;
    
    @Column(name = "tc_title_id")
    private String titleid;
    
    @Column(name = "tc_director_id", columnDefinition = "TEXT")
    private String directorId;
    
    @Column(name = "tc_writer_id", columnDefinition = "TEXT")
    private String writerId;

    public String getTitleid() {
        return this.titleid;
    }

    public void setTitleid(String titleid) {
        this.titleid = titleid;
    }

    public String getDirectorId() {
        return this.directorId;
    }

    public void setDirectorId(String directorId) {
        this.directorId = directorId;
    }

    public String getWriterId() {
        return this.writerId;
    }

    public void setWriterId(String writerId) {
        this.writerId = writerId;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
