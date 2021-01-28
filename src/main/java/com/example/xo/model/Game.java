package com.example.xo.model;

import javax.persistence.*;

@Entity
public class Game {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private String gameTable;
  private String gamecode;
  private char nextPlayer;
  private Character winner;
  @Transient
  private StringBuilder changeableGameTable;

  public Game() {
  }

  public Game(String gamecode) {
    this.gameTable = "-----" +
        "-----" +
        "-----" +
        "-----" +
        "-----";
    this.gamecode = gamecode;
    this.nextPlayer = 'x';
  }

  @PostLoad
  public void onload(){
    changeableGameTable = new StringBuilder(gameTable);
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

  public Character getWinner() {
    return winner;
  }

  public void setWinner(Character winner) {
    this.winner = winner;
  }

  public char getAt(int row, int column) {
    return this.gameTable.charAt(row * 5 + column);
  }

  public void setAT(int row, int column){
    int start = (row*5) + column;
    this.changeableGameTable.replace(start, start+1, String.valueOf(this.nextPlayer));
  }

  public void setWinnerToActualPlayer(){
    setWinner(nextPlayer);
  }

  public void saveNewGameTable(){
    setGameTable(changeableGameTable.toString());
  }
}
