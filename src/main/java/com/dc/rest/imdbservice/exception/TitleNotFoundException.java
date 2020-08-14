package com.dc.rest.imdbservice.exception;
/***
 ** Author: Dominic Coutinho
 ** Description: Exception class for invalid titles
 */
public class TitleNotFoundException extends ImdbServiceException {

  private static final long serialVersionUID = 1L;

  public TitleNotFoundException(String message) {
    super(message);
  }

  public TitleNotFoundException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public TitleNotFoundException(Throwable throwable) {
    super(throwable);
  }

}
