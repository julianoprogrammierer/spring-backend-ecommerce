package com.devsuperior.dscatalog.resources.exceptions;

import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import java.time.Instant;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * The type Resource exception handler.
 */
@ControllerAdvice
public class ResourceExceptionHandler {
  /**
   * Entity not found response entity.
   *
   * @param e       the e
   * @param request the request
   * @return the response entity
   */
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<StandardError> entityNotFound(EntityNotFoundException e,
                                                      HttpServletRequest request) {
    StandardError err = new StandardError();
    err.setTimestamp(Instant.now());
    err.setStatus(HttpStatus.NOT_FOUND.value());
    err.setError("Resource not found");
    err.setMessage(e.getMessage());
    err.setPath(request.getRequestURI());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
  }
}
