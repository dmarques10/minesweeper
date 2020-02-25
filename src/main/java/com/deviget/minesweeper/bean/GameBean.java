package com.deviget.minesweeper.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.deviget.minesweeper.repository.model.Cell;
import com.deviget.minesweeper.repository.model.GameStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameBean {

	private Long id;
	private Long rows;
	private Long columns;
	private Long mines;
	private GameStatus gameStatus;
	private List<Cell> cells;
}
