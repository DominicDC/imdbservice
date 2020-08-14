package com.dc.rest.imdbservice.rest.assembler;

import com.dc.rest.imdbservice.entity.Ratings;
import com.dc.rest.imdbservice.rest.controller.ImdbController;
import com.dc.rest.imdbservice.rest.resources.RatingsResourceInput;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

/***
 ** Author: Dominic Coutinho
 ** Description: This class is responsible for converting the rating domain object into rest input resource
 */
public class RatingsInputAssembler extends RepresentationModelAssemblerSupport<Ratings, RatingsResourceInput> {

  public RatingsInputAssembler() {
    super(ImdbController.class, RatingsResourceInput.class);
  }

//  public RatingsResourceInput toResource(Ratings ratings) {
//    RatingsResourceInput resource;
//    resource = createResourceWithId(ratings.getTitleId(), ratings);
//    BeanUtils.copyProperties(ratings, resource);
//    return resource;
//  }

  @Override
  public RatingsResourceInput toModel(Ratings ratings) {
    RatingsResourceInput resource = createModelWithId(ratings.getTitleId(), ratings);
    BeanUtils.copyProperties(ratings, resource);
    return resource;
  }
}
