package com.dc.rest.imdbservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dc.rest.imdbservice.entity.TitleBasics;
/***
 ** Author: Dominic Coutinho
 ** Description: TitleBasicsRepository skeleton
 */
public interface TitleBasicsRepository extends JpaRepository<TitleBasics, String> {
   // TitleBasics findById(@Param("id") String id);
}
