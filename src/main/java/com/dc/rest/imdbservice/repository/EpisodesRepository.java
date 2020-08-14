package com.dc.rest.imdbservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.dc.rest.imdbservice.entity.Episodes;
import com.dc.rest.imdbservice.entity.TitleBasics;
/***
 ** Author: Dominic Coutinho
 ** Description: EpisodesRepository skeleton
 */
public interface EpisodesRepository extends JpaRepository<Episodes, String> {
    TitleBasics findByTitleId(@Param("titleId") String titleId);
}
