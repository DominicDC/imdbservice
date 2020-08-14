package com.dc.rest.imdbservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dc.rest.imdbservice.entity.CastDetails;
/***
 ** Author: Dominic Coutinho
 ** Description: CastDetailsRepository skeleton
 */
public interface CastDetailsRepository extends JpaRepository<CastDetails, String> {
   // TitleBasics findById(@Param("id") String id);
}
