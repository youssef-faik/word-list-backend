package com.example.wordsservice.validation;

public class FieldError {
  private final String field;
  private final String errorMessage;
  private final Object rejectedValue;
  
  FieldError(String field, String errorMessage, Object rejectedValue) {
    this.field = field;
    this.errorMessage = errorMessage;
    this.rejectedValue = rejectedValue;
  }
  
  public String getField() {
    return field;
  }
  
  public String getErrorMessage() {
    return errorMessage;
  }
  
  public Object getRejectedValue() {
    return rejectedValue;
  }
}
