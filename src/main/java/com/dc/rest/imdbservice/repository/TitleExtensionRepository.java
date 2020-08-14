package com.dc.rest.imdbservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dc.rest.imdbservice.entity.TitleExtension;
/***
 ** Author: Dominic Coutinho
 ** Description: TitleExtensionRepository skeleton
 */
public interface TitleExtensionRepository extends JpaRepository<TitleExtension, String> {
   // TitleBasics findById(@Param("id") String id);
}
