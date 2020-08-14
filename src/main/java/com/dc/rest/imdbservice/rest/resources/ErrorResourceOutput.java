package com.dc.rest.imdbservice.rest.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/***
 ** Author: Dominic Coutinho
 ** Description: DTO for error resource output
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResourceOutput extends RepresentationModel {

  private String timestamp;
  private String path;
  private List<Map<String, String>> errors = new ArrayList<Map<String, String>>();

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public List<Map<String, String>> getErrors() {
    return errors;
  }

  public void setErrors(List<Map<String, String>> errors) {
    this.errors = errors;
  }

}
