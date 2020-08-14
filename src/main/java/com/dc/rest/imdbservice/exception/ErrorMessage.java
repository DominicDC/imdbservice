package com.dc.rest.imdbservice.exception;

/***
 ** Author: Dominic Coutinho
 ** Description: Error message definitions
 */

public class ErrorMessage {
  public static final String NONEXISTANT_JOB = "The job you requested does not exist.";
  public static final String UNABLE_TO_ACCESS_URL = "The URL is not accessible.Please check the URL";
  public static final String INVALID_URL = "The URL is not empty or null.";
  public static final String DOWNLOAD_ERROR = "Error occured while trying to download the files.";
  public static final String PARSING_ERROR = "Error occured while trying to parse the files.";
  public static final String IMDB_ERROR = "Error occured while trying to access imbd.";
  public static final String INGESTION_ERROR = "Error occured while trying to ingest data from imdb";
  public static final String TITLE_NOT_FOUND = "Title Id not found.";
}
