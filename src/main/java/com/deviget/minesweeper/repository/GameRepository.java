package com.deviget.minesweeper.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deviget.minesweeper.repository.model.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
}
