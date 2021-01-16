package com.example.xo.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MissingDataException extends Exception {

  public MissingDataException() {
    super("Some data is missing.");
  }

}
