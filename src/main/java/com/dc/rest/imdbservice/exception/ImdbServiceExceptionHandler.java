package com.dc.rest.imdbservice.exception;

import com.dc.rest.imdbservice.rest.resources.ErrorResourceOutput;
import org.apache.tomcat.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
/***
 ** Author: Dominic Coutinho
 ** Description: Exception handler for IMDB service
 */

@ControllerAdvice
public class ImdbServiceExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ImdbServiceExceptionHandler.class);

  private static final String MESSAGE = "message";

  private static final String CODE = "code";

  @ExceptionHandler(value = FileParsingException.class)
  @ResponseBody
  public ResponseEntity<Object> handleFileParsingException(FileParsingException exception) {
    LOGGER.info("Handling FileParsingException");
    return handleExceptionInternal(exception, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(value = TitleNotFoundException.class)
  @ResponseBody
  public ResponseEntity<Object> handleTitleNotFoundException(TitleNotFoundException exception) {
    LOGGER.info("Handling TitleNotFoundException");
    return handleExceptionInternal(exception, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handler for JobServiceException.
   *
   * @param exception
   * @return
   */
  @ExceptionHandler(value = ImdbServiceException.class)
  @ResponseBody
  public ResponseEntity<Object> handleImdbServiceException(ImdbServiceException exception) {
    LOGGER.info("Handling ImdbServiceException");
    return handleExceptionInternal(exception, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Handler for generic exception.
   *
   * @param exception
   * @return
   */
  @ExceptionHandler(value = Exception.class)
  @ResponseBody
  public ResponseEntity<Object> handleException(Exception exception) {
    LOGGER.info("Handling Exception");
    return handleExceptionInternal(exception, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Single place to customize the response body of all Exception types.
   *
   * @param exception
   * @return
   */
  public ResponseEntity<Object> handleExceptionInternal(Exception exception,
      HttpStatus httpStatus) {
    ExceptionUtils exceptionUtils = new ExceptionUtils();

    exception.printStackTrace();
    // Log the exception
    logException(exception);

    // Set the error message in the object
    ErrorResourceOutput errorResourceOutput = setErrorObject(exception.getMessage(), httpStatus);

    // Get the status code from request
    HttpStatus status = getStatus(httpStatus);
    return new ResponseEntity<>(errorResourceOutput, status);
  }

  /**
   * Method to log exceptions.
   * 
   * @param exception
   */
  public void logException(Exception exception) {
    String errorMsg = exception.getMessage();
    LOGGER.error("Error Msg : " + errorMsg);
    LOGGER.error("Stack Trace : " + exception);
  }

  /**
   * Utility method to set the error msg object.
   * 
   * @param message
   * @return
   */
  public ErrorResourceOutput setErrorObject(String message, HttpStatus httpStatus) {

    ErrorResourceOutput errorResourceOutput = new ErrorResourceOutput();

    // Setting the path for the json output
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

    if (request != null) {
      String url = request.getRequestURL().toString();
      errorResourceOutput.setPath(url);

      // Setting the timestamp for the json output
      errorResourceOutput.setTimestamp(getUTCDateString(new Date()));

      // Set the error code and message
      HttpStatus status = getStatus(httpStatus);
      String code = String.valueOf(status.value());

      List<Map<String, String>> errors = errorResourceOutput.getErrors();
      Map<String, String> errorMap = new HashMap<>();
      errorMap.put(MESSAGE, message);
      errorMap.put(CODE, code);
      errors.add(errorMap);
    }

    LOGGER.info("errorResourceOutput" + errorResourceOutput);
    return errorResourceOutput;
  }

  /**
   * Method to retrieve the status attribute from the request context.
   * 
   * @param httpStatus
   * @return
   */
  public HttpStatus getStatus(HttpStatus httpStatus) {
    if (httpStatus == null) {
      return HttpStatus.INTERNAL_SERVER_ERROR;
    }
    return httpStatus;
  }

  /**
   * Return an ISO 8601 String representation of a date in UTC.
   *
   * @param date
   * @return
   */
  public static String getUTCDateString(Date date) {
    String stringRepresentation = null;
    if (date != null) {
      ZoneId utcZoneId = ZoneId.of("Z");
      DateTimeFormatter formatter =
          DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX", Locale.US);
      ZonedDateTime zonedDateTime = date.toInstant().atZone(utcZoneId);
      stringRepresentation = zonedDateTime.format(formatter);
    }
    return stringRepresentation;
  }


}
