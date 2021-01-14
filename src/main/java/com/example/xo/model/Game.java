package com.example.xo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Game {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private String gameTable;
  private String gamecode;
  private char nextPlayer;

  public Game() {
  }

  public Game(String gamecode) {
    this.gameTable = "----- " +
        "----- " +
        "----- " +
        "----- " +
        "-----";
    this.gamecode = gamecode;
    this.nextPlayer = 'x';
  }

  public String getGamecode() {
    return gamecode;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getGameTable() {
    return gameTable;
  }

  public void setGameTable(String gameTable) {
    this.gameTable = gameTable;
  }

  public void setGamecode(String gamecode) {
    this.gamecode = gamecode;
  }

  public char getNextPlayer() {
    return nextPlayer;
  }

  public void setNextPlayer(char nextPlayer) {
    this.nextPlayer = nextPlayer;
  }
}
