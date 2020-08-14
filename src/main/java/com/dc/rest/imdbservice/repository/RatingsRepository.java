package com.dc.rest.imdbservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dc.rest.imdbservice.entity.Ratings;
/***
 ** Author: Dominic Coutinho
 ** Description: RatingsRepository skeleton
 */
public interface RatingsRepository extends JpaRepository<Ratings, String> {
    Ratings findByTitleId(String titleId);
}
