package com.reservation.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionController {

  @ExceptionHandler({
      Exception.class
  })

  public ResponseEntity<ExceptionResponse> customRequestException(final Exception c){

    log.warn("api Exception : {}", c.getErrorCode());

    return ResponseEntity.badRequest().body(
        new ExceptionResponse(c.getMessage(), c.getErrorCode())
    );
  }

  @Getter
  @ToString
  @AllArgsConstructor
  public static class ExceptionResponse{
    private String message;
    private ErrorCode errorCode;
  }
}
