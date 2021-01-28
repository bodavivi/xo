package com.example.xo.model;

public class Step {

  private String gamecode;
  private Integer row;
  private Integer column;

  public Step() {
  }

  public String getGamecode() {
    return gamecode;
  }

  public void setGamecode(String gamecode) {
    this.gamecode = gamecode;
  }

  public Integer getRow() {
    return row;
  }

  public void setRow(Integer row) {
    this.row = row;
  }

  public Integer getColumn() {
    return column;
  }

  public void setColumn(Integer column) {
    this.column = column;
  }
}
