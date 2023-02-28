package com.example.wordsservice;

import com.example.wordsservice.exceptions.WordNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("unused")
@ControllerAdvice
class ApiControllerAdvice {
  
  @ResponseBody
  @ExceptionHandler(WordNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String wordNotFoundHandler(WordNotFoundException ex) {
    return ex.getMessage();
  }
}
