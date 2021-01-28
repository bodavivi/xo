package com.example.xo.service;

import com.example.xo.exceptions.GameIsNotCreatedException;
import com.example.xo.exceptions.GameIsNotFoundException;
import com.example.xo.exceptions.MissingDataException;
import com.example.xo.exceptions.UnavailablePositionException;
import com.example.xo.model.Game;
import com.example.xo.model.Step;
import com.example.xo.repository.GameRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class GameServiceTest {

  @Mock
  private GameRepository gameRepository;

  @InjectMocks
  private GameService gameService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void takeAStep_should_throw_exception_if_gamecode_is_wrong() throws Exception {
    when(gameRepository.findGameByGamecode(any())).thenReturn(null);
    Step step = new Step();
    step.setGamecode("nonexist");
    step.setRow(3);
    step.setColumn(3);
    Assertions.assertThrows(GameIsNotFoundException.class, () -> {
      gameService.takeAStep(step);
    });
  }

  @Test
  public void takeAStep_should_throw_exception_if_step_is_already_reserved() throws Exception {
    Game game = new Game();
    game.setNextPlayer('x');
    game.setGamecode("12345678");
    game.setGameTable("------------o------------");
    when(gameRepository.findGameByGamecode(any())).thenReturn(game);
    Step step = new Step();
    step.setGamecode("12345678");
    step.setRow(2);
    step.setColumn(2);
    Assertions.assertThrows(UnavailablePositionException.class, () -> {
      gameService.takeAStep(step);
    });
  }

  @Test
  public void takeAStep_should_throw_exception_if_column_is_bigger_than_5() throws Exception {
    Game game = new Game();
    game.setNextPlayer('x');
    game.setGamecode("12345678");
    game.setGameTable("------------o------------");
    when(gameRepository.findGameByGamecode(any())).thenReturn(game);
    Step step = new Step();
    step.setGamecode("12345678");
    step.setRow(3);
    step.setColumn(6);
    Assertions.assertThrows(UnavailablePositionException.class, () -> {
      gameService.takeAStep(step);
    });
  }

  @Test
  public void takeAStep_should_throw_exception_if_row_is_bigger_than_5() throws Exception {
    Game game = new Game();
    game.setNextPlayer('x');
    game.setGamecode("12345678");
    game.setGameTable("------------o------------");
    when(gameRepository.findGameByGamecode(any())).thenReturn(game);
    Step step = new Step();
    step.setGamecode("12345678");
    step.setRow(6);
    step.setColumn(1);
    Assertions.assertThrows(UnavailablePositionException.class, () -> {
      gameService.takeAStep(step);
    });
  }

  @Test
  public void takeAStep_should_throw_exception_if_row_is_smaller_than_1() throws Exception {
    Game game = new Game();
    game.setNextPlayer('x');
    game.setGamecode("12345678");
    game.setGameTable("------------o------------");
    when(gameRepository.findGameByGamecode(any())).thenReturn(game);
    Step step = new Step();
    step.setGamecode("12345678");
    step.setRow(-2);
    step.setColumn(1);
    Assertions.assertThrows(UnavailablePositionException.class, () -> {
      gameService.takeAStep(step);
    });
  }

  @Test
  public void takeAStep_should_throw_exception_if_column_is_smaller_than_1() throws Exception {
    Game game = new Game();
    game.setNextPlayer('x');
    game.setGamecode("12345678");
    game.setGameTable("------------o------------");
    when(gameRepository.findGameByGamecode(any())).thenReturn(game);
    Step step = new Step();
    step.setGamecode("12345678");
    step.setRow(2);
    step.setColumn(-1);
    Assertions.assertThrows(UnavailablePositionException.class, () -> {
      gameService.takeAStep(step);
    });
  }

  @Test
  public void takeAStep_should_throw_exception_if_gamecode_is_missing() throws Exception {
    Step step = new Step();
    step.setRow(3);
    step.setColumn(3);
    Assertions.assertThrows(MissingDataException.class, () -> {
      gameService.takeAStep(step);
    });
  }

  @Test
  public void takeAStep_should_throw_exception_if_column_is_missing() throws Exception {
    Step step = new Step();
    step.setGamecode("gamecode");
    step.setRow(3);
    Assertions.assertThrows(MissingDataException.class, () -> {
      gameService.takeAStep(step);
    });
  }

  @Test
  public void takeAStep_should_throw_exception_if_row_is_missing() throws Exception {
    Step step = new Step();
    step.setGamecode("gamecode");
    step.setColumn(3);
    Assertions.assertThrows(MissingDataException.class, () -> {
      gameService.takeAStep(step);
    });
  }

  @Test
  public void getGame_should_throw_exception_if_gamecode_is_not_exist() {
    when(gameRepository.findGameByGamecode(any())).thenReturn(null);
    Assertions.assertThrows(GameIsNotFoundException.class, () -> {
      gameService.getGame("gamecode");
    });
  }

  @Test
  public void createGame_should_throw_exception_if_game_is_not_created() {
    when(gameRepository.save(any())).thenReturn(null);
    Assertions.assertThrows(GameIsNotCreatedException.class, () -> {
      gameService.createGame();
    });
  }

  @Test
  public void createGame_should_give_back_game() throws GameIsNotCreatedException {
    Game game = new Game();
    game.setGameTable("-------------------------");
    game.setNextPlayer('x');
    game.setGamecode("gamecode");
    when(gameRepository.save(any())).thenReturn(game);
    Game result = gameService.createGame();
    Assertions.assertNotNull(result);
    Assertions.assertEquals(result.getGamecode(), "gamecode");
    Assertions.assertEquals(result.getNextPlayer(), 'x');
    Assertions.assertEquals(result.getGameTable(), "-------------------------");
    Assertions.assertNull(result.getWinner());
  }

  @Test
  public void takeAStep_should_draw_step_in_the_right_place_and_change_next_player() throws Exception {
    Game game = new Game();
    game.setGameTable("--x---o----xo------x-----");
    game.setNextPlayer('o');
    when(gameRepository.findGameByGamecode(any())).thenReturn(game);
    Step step = new Step();
    step.setGamecode("gamecode");
    step.setColumn(3);
    step.setRow(2);
    Game result = gameService.takeAStep(step);
    Assertions.assertNotNull(result);
    Assertions.assertEquals("--x---o----xoo-----x-----", result.getGameTable());
    Assertions.assertEquals('x', result.getNextPlayer());
    Assertions.assertNull(result.getWinner());
  }

  @Test
  public void takeAStep_should_draw_step_in_the_right_place_and_change_next_player_and_change_winner_if_there_are_three_os_in_one_row() throws Exception {
    Game game = new Game();
    game.setGameTable("--x---o-o--xo------x-----");
    game.setNextPlayer('o');
    when(gameRepository.findGameByGamecode(any())).thenReturn(game);
    Step step = new Step();
    step.setGamecode("gamecode");
    step.setColumn(2);
    step.setRow(1);
    Game result = gameService.takeAStep(step);
    Assertions.assertNotNull(result);
    Assertions.assertEquals("--x---ooo--xo------x-----", result.getGameTable());
    Assertions.assertEquals('x', result.getNextPlayer());
    Assertions.assertEquals('o', result.getWinner());
  }

  @Test
  public void takeAStep_should_draw_step_in_the_right_place_and_change_next_player_and_change_winner_if_there_are_three_xs_in_one_column() throws Exception {
    Game game = new Game();
    game.setGameTable("-xx----oo--xo-o----x-----");
    game.setNextPlayer('x');
    when(gameRepository.findGameByGamecode(any())).thenReturn(game);
    Step step = new Step();
    step.setGamecode("gamecode");
    step.setColumn(1);
    step.setRow(1);
    Game result = gameService.takeAStep(step);
    Assertions.assertNotNull(result);
    Assertions.assertEquals("-xx---xoo--xo-o----x-----", result.getGameTable());
    Assertions.assertEquals('o', result.getNextPlayer());
    Assertions.assertEquals('x', result.getWinner());
  }

  @Test
  public void takeAStep_should_draw_step_in_the_right_place_and_change_next_player_and_change_winner_if_there_are_three_xs_in_a_diagonal_when_row_is_not_bigger_than_column() throws Exception {
    Game game = new Game();
    game.setGameTable("-xx-----oo--oxo----x----");
    game.setNextPlayer('x');
    when(gameRepository.findGameByGamecode(any())).thenReturn(game);
    Step step = new Step();
    step.setGamecode("gamecode");
    step.setColumn(2);
    step.setRow(1);
    Game result = gameService.takeAStep(step);
    Assertions.assertNotNull(result);
    Assertions.assertEquals("-xx----xoo--oxo----x----", result.getGameTable());
    Assertions.assertEquals('o', result.getNextPlayer());
    Assertions.assertEquals('x', result.getWinner());
  }

  @Test
  public void takeAStep_should_draw_step_in_the_right_place_and_change_next_player_and_change_winner_there_are_three_os_in_a_diagonal_when_row_is_bigger_than_column() throws Exception {
    Game game = new Game();
    game.setGameTable("-xx--------------o--x--o-");
    game.setNextPlayer('o');
    when(gameRepository.findGameByGamecode(any())).thenReturn(game);
    Step step = new Step();
    step.setGamecode("gamecode");
    step.setColumn(1);
    step.setRow(2);
    Game result = gameService.takeAStep(step);
    Assertions.assertNotNull(result);
    Assertions.assertEquals("-xx--------o-----o--x--o-", result.getGameTable());
    Assertions.assertEquals('x', result.getNextPlayer());
    Assertions.assertEquals('o', result.getWinner());
  }
}