package com.deviget.minesweeper.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deviget.minesweeper.repository.model.Game;
import com.deviget.minesweeper.repository.model.GameStatus;

public interface GameRepository extends JpaRepository<Game, Long> {
	Optional<Game> findByUserNameAndGameStatus(String userName, GameStatus gameStatus);
}
