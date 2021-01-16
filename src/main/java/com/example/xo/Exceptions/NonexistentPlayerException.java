package com.example.xo.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class NonexistentPlayerException extends Exception {

  public NonexistentPlayerException(char player) {
    super("You, " + player + " are is not even play.");
  }
}
