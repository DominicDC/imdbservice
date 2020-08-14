package com.dc.rest.imdbservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/***
 ** Author: Dominic Coutinho
 ** Description: This pojo defines the properties of cast members
 */

@Entity
@Table(name = "name_basics", schema = "imdb")
public class CastDetails {
    
    @Id
    @Column(name = "nb_cast_id")
    private String castId;

    @Column(name = "nb_primary_name")
    private String primaryName;
    
    @Column(name = "nb_birth_year")
    private Integer birthYear;
    
    @Column(name = "nb_death_year")
    private Integer deathYear;

    @Column(name = "nb_primary_profession")
    private String profession;
    
    @Column(name = "nb_known_for_titles", columnDefinition="TEXT")
    private String knownForTitles;

    public String getCastId() {
        return this.castId;
    }

    public void setCastId(String castId) {
        this.castId = castId;
    }

    public String getPrimaryName() {
        return this.primaryName;
    }

    public void setPrimaryName(String primaryName) {
        this.primaryName = primaryName;
    }

    public Integer getBirthYear() {
        return this.birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public Integer getDeathYear() {
        return this.deathYear;
    }

    public void setDeathYear(Integer deathYear) {
        this.deathYear = deathYear;
    }

    public String getProfession() {
        return this.profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getKnownForTitles() {
        return this.knownForTitles;
    }

    public void setKnownForTitles(String knownForTitles) {
        this.knownForTitles = knownForTitles;
    }

}
