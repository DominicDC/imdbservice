package com.dc.rest.imdbservice.exception;
/***
 ** Author: Dominic Coutinho
 ** Description: File parsing exception class
 */

public class FileParsingException extends ImdbServiceException {

  private static final long serialVersionUID = 8886189429868707460L;

  public FileParsingException(String message) {
    super(message);
  }

  public FileParsingException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public FileParsingException(Throwable throwable) {
    super(throwable);
  }
}
