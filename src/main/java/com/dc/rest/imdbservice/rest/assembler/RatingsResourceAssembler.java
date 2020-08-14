package com.dc.rest.imdbservice.rest.assembler;

import org.springframework.beans.BeanUtils;

import com.dc.rest.imdbservice.entity.Ratings;
import com.dc.rest.imdbservice.rest.controller.ImdbController;
import com.dc.rest.imdbservice.rest.resources.RatingsResourceOutput;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

/***
 ** Author: Dominic Coutinho
 ** Description: This class is responsible for converting the rating domain object into rest output resource
 */
public class RatingsResourceAssembler extends RepresentationModelAssemblerSupport<Ratings, RatingsResourceOutput> {

  public RatingsResourceAssembler() {
    super(ImdbController.class, RatingsResourceOutput.class);
  }

//  public RatingsResourceOutput toResource(Ratings ratings) {
//
//    RatingsResourceOutput resource;
//    resource = createResourceWithId(ratings.getTitleId(), ratings);
//    BeanUtils.copyProperties(ratings, resource);
//    return resource;
//  }

//  @Override
//  public RatingsResourceOutput toModel(RatingsResourceOutput ratings) {
//
//    RatingsResourceOutput resource = createModelWithId(ratings.getTitleId(), ratings);
//    // … do further mapping
//    BeanUtils.copyProperties(ratings, resource);
//    return resource;
//  }

  @Override
  public RatingsResourceOutput toModel(Ratings ratings) {
    RatingsResourceOutput resource = createModelWithId(ratings.getTitleId(), ratings);
    BeanUtils.copyProperties(ratings, resource);
    return resource;
  }
}
