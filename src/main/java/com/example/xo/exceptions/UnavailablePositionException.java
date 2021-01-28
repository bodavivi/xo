package com.example.xo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnavailablePositionException extends Exception {

  public UnavailablePositionException(int row, int column) {
    super("You can't choose position: row = " + (row) + ", column = " + (column));
  }

}
