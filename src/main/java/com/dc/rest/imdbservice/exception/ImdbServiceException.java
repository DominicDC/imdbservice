package com.dc.rest.imdbservice.exception;
/***
 ** Author: Dominic Coutinho
 ** Description: Custom base exception class
 */

public class ImdbServiceException extends RuntimeException {

  private static final long serialVersionUID = 8886189429868707460L;

  public ImdbServiceException(String message) {
    super(message);
  }

  public ImdbServiceException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public ImdbServiceException(Throwable throwable) {
    super(throwable);
  }
}
