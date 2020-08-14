package com.dc.rest.imdbservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dc.rest.imdbservice.entity.CastCrew;
/***
 ** Author: Dominic Coutinho
 ** Description: CastCrewRepository skeleton
 */
public interface CastCrewRepository extends JpaRepository<CastCrew, String> {
   // TitleBasics findById(@Param("id") String id);
}
