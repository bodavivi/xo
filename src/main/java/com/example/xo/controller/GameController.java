package com.example.xo.controller;

import com.example.xo.model.Game;
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
  public ResponseEntity createGame() {
    Game game = gameService.createGame();
    if (game != null) {
      return ResponseEntity.status(HttpStatus.CREATED).body("Gamecode: " + game.getGamecode());
    }
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Game isn't created.");
  }

  @GetMapping("/{gamecode}")
  public ResponseEntity getGame(@PathVariable String gamecode) throws Exception {
    Game game = gameService.getGame(gamecode);
    if (game != null) {
      return ResponseEntity.status(HttpStatus.OK).body(game);
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game with " + gamecode + " gamecode doesn't exist.");
  }

  @GetMapping("/step/{gamecode}")
  public ResponseEntity takeAStep(@PathVariable String gamecode, @RequestParam char player, @RequestParam int row, @RequestParam int column) throws Exception {
    Game game = gameService.takeAStep(gamecode, player, row, column);
    return ResponseEntity.status(HttpStatus.OK).body(game);
  }
}
