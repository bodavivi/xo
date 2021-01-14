package com.example.xo.repository;

import com.example.xo.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

  Game findGameByGamecode(String gamecode);
}
