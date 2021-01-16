package com.example.xo.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class NotYourTurnException extends Exception{

  public NotYourTurnException() {
    super("It is not your turn");
  }

}
