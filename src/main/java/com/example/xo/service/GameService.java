package com.example.xo.service;

import com.example.xo.exceptions.*;
import com.example.xo.model.Game;
import com.example.xo.model.Step;
import com.example.xo.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GameService {

  private GameRepository gameRepository;

  @Autowired
  public GameService(GameRepository gameRepository) {
    this.gameRepository = gameRepository;
  }


  public Game createGame() throws GameIsNotCreatedException {
    Game game = gameRepository.save(new Game(generateGamecode()));
    if (game == null) {
      throw new GameIsNotCreatedException();
    }
    return game;
  }

  private String generateGamecode() {
    String gamecode = UUID.randomUUID().toString().substring(0, 8);
    while (gameRepository.findGameByGamecode(gamecode) != null) {
      gamecode = UUID.randomUUID().toString().substring(0, 8);
    }
    return gamecode;
  }

  public Game getGame(String gamecode) throws GameIsNotFoundException {
    Game game = gameRepository.findGameByGamecode(gamecode);
    if (game == null) {
      throw new GameIsNotFoundException(gamecode);
    }
    return game;
  }

  public Game takeAStep(Step step) throws Exception {
    if (step.getColumn() == null || step.getRow() == null || step.getGamecode() == null || step.getPlayer() == null)
      throw new MissingDataException();
    if (step.getRow() > 5 || step.getRow() < 1 || step.getColumn() > 5 || step.getColumn() < 1)
      throw new UnavaliablePositionException(step.getRow(), step.getColumn());
    String gamecode = step.getGamecode();
    Game game = getGame(gamecode);
    if (game == null) throw new GameIsNotFoundException(gamecode);
    char player = step.getPlayer();
    int row = step.getRow() - 1;
    int column = step.getColumn() - 1;
    validateNextPlayer(game.getNextPlayer(), player);
    String[] rows = game.getGameTable().split(" ");
    positionAvailabilityChecker(rows, row, column);
    drawStep(game, rows, row, column);
    if (checkWinner(player, rows, row, column)) game.setWinner(player);
    game.setNextPlayer(changeNextPlayer(game.getNextPlayer()));
    gameRepository.save(game);
    return game;
  }

  private void validateNextPlayer(char nextPlayer, char actualPlayer) throws Exception {
    if (actualPlayer != 'o' && actualPlayer != 'x') throw new NonexistentPlayerException(actualPlayer);
    if (nextPlayer != actualPlayer) throw new NotYourTurnException();
  }

  private void positionAvailabilityChecker(String[] rows, int row, int column) throws UnavaliablePositionException {
    if (rows[row].charAt(column) != '-') throw new UnavaliablePositionException(row + 1, column + 1);
  }

  private void drawStep(Game game, String[] rows, int row, int column) {
    String actualRow = rows[row];
    actualRow = actualRow.substring(0, column) + game.getNextPlayer() + actualRow.substring(column + 1);
    rows[row] = actualRow;
    String gameTable = String.join(" ", rows);
    game.setGameTable(gameTable);
  }

  private char changeNextPlayer(char player) {
    if (player == 'x') return 'o';
    return 'x';
  }

  private boolean checkWinner(char player, String[] rows, int row, int column) {
    String winner = (player == 'x') ? ("xxx") : ("ooo");
    return (checkRow(rows[row], winner)) || (checkColumn(rows, column, winner)) || (checkDiagonals(rows, row, column, winner));
  }

  private boolean checkRow(String row, String winner) {
    return row.contains(winner);
  }

  private boolean checkColumn(String[] rows, int column, String winner) {
    StringBuilder columnBuilder = new StringBuilder();
    for (String row : rows) {
      columnBuilder.append(row.charAt(column));
    }
    return columnBuilder.toString().contains(winner);
  }

  private boolean checkDiagonals(String[] rows, int row, int column, String winner) {
    if (checkFirstDiagonal(rows, row, column, winner)) return true;
    return checkSecondDiagonal(rows, row, column, winner);
  }

  private boolean checkFirstDiagonal(String[] rows, int row, int column, String winner) {
    StringBuilder diagonalBuilder = new StringBuilder();
    if (row > column) {
      int actualRow = row - column;
      int actualColumn = 0;
      for (int i = actualRow; i < 5; i++) {
        diagonalBuilder.append(rows[i].charAt(actualColumn));
        actualColumn++;
      }
    } else {
      int actualColumn = column - row;
      int actualRow = 0;
      for (int i = actualColumn; i < 5; i++) {
        diagonalBuilder.append(rows[actualRow].charAt(i));
        actualRow++;
      }
    }
    return diagonalBuilder.toString().contains(winner);
  }

  private boolean checkSecondDiagonal(String[] rows, int row, int column, String winner) {
    StringBuilder diagonalBuilder = new StringBuilder();
    if (row > column) {
      int diff = 4 - row;
      int actualRow = row + diff;
      int actualColumn = column - diff;
      while (actualRow >= 0 || actualColumn < 5) {
        if (actualColumn >= 0 && actualColumn < 5 && actualRow >= 0 && actualRow < 5) {
          diagonalBuilder.append(rows[actualRow].charAt(actualColumn));
        }
        actualRow--;
        actualColumn++;
      }
    } else {
      int diff = 4 - column;
      int actualColumn = column + diff;
      int actualRow = row - diff;
      while (actualColumn >= 0 || actualRow < 5) {
        if (actualColumn >= 0 && actualColumn < 5 && actualRow >= 0 && actualRow < 5) {
          diagonalBuilder.append(rows[actualRow].charAt(actualColumn));
        }
        actualColumn--;
        actualRow++;
      }
    }

    return diagonalBuilder.toString().contains(winner);
  }
}
