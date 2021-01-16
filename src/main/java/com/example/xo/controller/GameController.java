package com.example.xo.controller;

import com.example.xo.exceptions.GameIsNotCreatedException;
import com.example.xo.exceptions.GameIsNotFoundException;
import com.example.xo.model.Game;
import com.example.xo.model.Step;
import com.example.xo.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
public class GameController {

  private GameService gameService;

  @Autowired
  public GameController(GameService gameService) {
    this.gameService = gameService;
  }

  @GetMapping("/create")
  public ResponseEntity createGame() throws GameIsNotCreatedException {
    Game game = gameService.createGame();
    return ResponseEntity.status(HttpStatus.CREATED).body("Gamecode: " + game.getGamecode());
  }

  @GetMapping("/{gamecode}")
  public ResponseEntity getGame(@PathVariable String gamecode) throws GameIsNotFoundException {
    Game game = gameService.getGame(gamecode);
    return ResponseEntity.status(HttpStatus.OK).body(game);
  }

  @GetMapping("/step")
  public ResponseEntity takeAStep(@RequestBody Step step) throws Exception {
    Game game = gameService.takeAStep(step);
    if (game.getWinner() != null) {
      return ResponseEntity.status(HttpStatus.OK).body("The winner is: " + game.getWinner());
    }
    return ResponseEntity.status(HttpStatus.OK).body(game);
  }
}
