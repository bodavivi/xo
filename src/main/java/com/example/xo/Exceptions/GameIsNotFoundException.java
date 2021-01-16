package com.example.xo.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GameIsNotFoundException extends Exception {

  public GameIsNotFoundException(String gamecode) {
    super("Game with " + gamecode + " gamecode is not found");
  }

}
