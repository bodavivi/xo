package com.example.xo.service;

import com.example.xo.model.Game;
import com.example.xo.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GameService {

  private final GameRepository gameRepository;

  @Autowired
  public GameService(GameRepository gameRepository) {
    this.gameRepository = gameRepository;
  }


  public Game createGame() {
    return gameRepository.save(new Game(generateGamecode()));
  }

  private String generateGamecode() {
    String gamecode = UUID.randomUUID().toString().substring(0, 8);
    while (gameRepository.findGameByGamecode(gamecode) != null) {
      gamecode = UUID.randomUUID().toString().substring(0, 8);
    }
    return gamecode;
  }

  public Game getGame(String gamecode) throws Exception {
    Game game = gameRepository.findGameByGamecode(gamecode);
    if (game == null) {
      throw new Exception("Game is not found.");
    }
    return game;
  }

  public Game takeAStep(String gamecode, char player, int row, int column) throws Exception {
    Game game = getGame(gamecode);
    if (!actualPlayerIsTheNextPlayer(game.getNextPlayer(), player)) {
      throw new Exception("It is not your turn.");
    }
    String[] rows = game.getGameTable().split(" ");
    if (!positionIsAvailable(rows, row, column)) {
      throw new Exception("You can't choose position: row = " + row + ", column = " + column);
    }
    drawStep(game, rows, row, column);
    return game;
  }

  private boolean actualPlayerIsTheNextPlayer(char nextPlayer, char actualPlayer) {
    if (nextPlayer != actualPlayer) return false;
    return true;
  }

  private boolean positionIsAvailable(String[] rows, int row, int column) {
    if (rows[row - 1].charAt(column - 1) != '-') return false;
    return true;
  }

  private void drawStep(Game game, String[] rows, int row, int column) {
    String actualRow = rows[row - 1];
    actualRow = actualRow.substring(0, column - 1) + game.getNextPlayer() + actualRow.substring(column);
    rows[row - 1] = actualRow;
    String gameTable = String.join(" ", rows);
    game.setGameTable(gameTable);
    gameRepository.save(game);
  }
}
