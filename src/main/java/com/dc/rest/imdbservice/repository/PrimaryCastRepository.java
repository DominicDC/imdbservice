package com.dc.rest.imdbservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dc.rest.imdbservice.entity.PrimaryCast;
/***
 ** Author: Dominic Coutinho
 ** Description: PrimaryCastRepository skeleton
 */
public interface PrimaryCastRepository extends JpaRepository<PrimaryCast, String> {
   // TitleBasics findById(@Param("id") String id);
}
