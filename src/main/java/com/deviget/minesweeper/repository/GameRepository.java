package com.deviget.minesweeper.repository;

import java.util.EnumSet;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.deviget.minesweeper.repository.model.Game;
import com.deviget.minesweeper.repository.model.GameStatus;

public interface GameRepository extends JpaRepository<Game, Long> {
	Optional<Game> findByUserNameAndGameStatus(String userName, GameStatus gameStatus);

	Optional<Game> findByUserNameAndGameStatusNotIn(String userName, EnumSet<GameStatus> status);

	@Modifying
	@Query("UPDATE Game g SET g.gameStatus = :gameStatus WHERE g.userName = :userName")
	void updateGameStatusByUserName(GameStatus gameStatus, String userName);

	Optional<Game> findByUserName(String userName);
}
