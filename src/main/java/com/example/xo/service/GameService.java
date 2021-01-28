package com.example.xo.service;

import com.example.xo.exceptions.GameIsNotCreatedException;
import com.example.xo.exceptions.GameIsNotFoundException;
import com.example.xo.exceptions.MissingDataException;
import com.example.xo.exceptions.UnavailablePositionException;
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
    if (step.getColumn() == null || step.getRow() == null || step.getGamecode() == null)
      throw new MissingDataException();
    if (step.getRow() > 4 || step.getRow() < 0 || step.getColumn() > 4 || step.getColumn() < 0)
      throw new UnavailablePositionException(step.getRow(), step.getColumn());
    String gamecode = step.getGamecode();
    Game game = getGame(gamecode);
    game.onload();
    if (game == null) throw new GameIsNotFoundException(gamecode);
    int row = step.getRow();
    int column = step.getColumn();
    positionAvailabilityChecker(game, row, column);
    game.setAT(row, column);
    game.saveNewGameTable();
    if (checkWinner(game, row, column)) game.setWinnerToActualPlayer();
    game.setNextPlayer(changeNextPlayer(game.getNextPlayer()));
    gameRepository.save(game);
    return game;
  }

  private void positionAvailabilityChecker(Game game, int row, int column) throws UnavailablePositionException {
    if (game.getAt(row, column) != '-') throw new UnavailablePositionException(row, column);
  }

  private char changeNextPlayer(char player) {
    if (player == 'x') return 'o';
    return 'x';
  }

  private boolean checkWinner(Game game, int row, int column) {
    String winner = (game.getNextPlayer() == 'x') ? ("xxx") : ("ooo");
    return (isThereWinnerInRow(game, winner, row)) || (isThereWinnerInColumn(game, column, winner)) || (isThereWinnerInDiagonals(game, row, column, winner));
  }

  private boolean isThereWinnerInRow(Game game, String winner, int row) {
    StringBuilder actualRow = new StringBuilder();
    for (int i = 0; i < 5; i++) {
      actualRow.append(game.getAt(row, i));
    }
    return actualRow.toString().contains(winner);
  }

  private boolean isThereWinnerInColumn(Game game, int column, String winner) {
    StringBuilder actualColumn = new StringBuilder();
    for (int i = 0; i < 5; i++) {
      actualColumn.append(game.getAt(i, column));
    }
    return actualColumn.toString().contains(winner);
  }

  private boolean isThereWinnerInDiagonals(Game game, int row, int column, String winner) {
    if (isThereWinnerInTheFirstDiagonal(game, row, column, winner)) return true;
    return isThereWinnerInTheSecondDiagonal(game, row, column, winner);
  }

  private boolean isThereWinnerInTheFirstDiagonal(Game game, int row, int column, String winner) {
    StringBuilder actualDiagonal = new StringBuilder();
    if (row > column) {
      int actualRow = row - column;
      int actualColumn = 0;
      for (int i = actualRow; i < 5; i++) {
        actualDiagonal.append(game.getAt(i, actualColumn));
        actualColumn++;
      }
    } else {
      int actualColumn = column - row;
      int actualRow = 0;
      for (int i = actualColumn; i < 5; i++) {
        actualDiagonal.append(game.getAt(actualRow, i));
        actualRow++;
      }
    }
    return actualDiagonal.toString().contains(winner);
  }

  private boolean isThereWinnerInTheSecondDiagonal(Game game, int row, int column, String winner) {
    StringBuilder diagonalBuilder = new StringBuilder();
    if (row > column) {
      int diff = 4 - row;
      int actualRow = row + diff;
      int actualColumn = column - diff;
      while (actualRow >= 0 || actualColumn < 5) {
        if (actualColumn >= 0 && actualColumn < 5 && actualRow >= 0 && actualRow < 5) {
          diagonalBuilder.append(game.getAt(actualRow, actualColumn));
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
          diagonalBuilder.append(game.getAt(actualRow, actualColumn));
        }
        actualColumn--;
        actualRow++;
      }
    }

    return diagonalBuilder.toString().contains(winner);
  }
}
