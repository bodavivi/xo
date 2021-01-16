package com.example.xo.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class GameIsNotCreatedException extends Exception {

  public GameIsNotCreatedException() {
    super("Game is not created.");
  }

}
