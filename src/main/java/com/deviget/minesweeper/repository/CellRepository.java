package com.deviget.minesweeper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.deviget.minesweeper.repository.model.Cell;
import com.deviget.minesweeper.repository.model.CellOperation;

public interface CellRepository extends JpaRepository<Cell, Long> {
	@Modifying
	@Query("UPDATE Cell c SET c.cellOperation= :cellOperation WHERE c.id = :id")
	void updateCellOperationById(@Param("cellOperation") CellOperation cellOperation, @Param("id") Long id);

	@Modifying
	@Query("UPDATE Cell c SET c.cellOperation = :cellOperation, c.minesAround = :minesAround WHERE c.id = :id")
	void updateCellOperationAndMinesAroundById(@Param("cellOperation") CellOperation cellOperation,
																						 @Param("minesAround") Long minesAround,
																						 @Param("id") Long id);
}
