package com.deviget.minesweeper.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deviget.minesweeper.repository.model.Cell;

public interface CellRepository extends JpaRepository<Cell, Long> {
}
