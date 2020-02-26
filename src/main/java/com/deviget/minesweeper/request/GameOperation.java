package com.deviget.minesweeper.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.deviget.minesweeper.bean.GameBean;
import com.deviget.minesweeper.repository.model.CellOperation;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GameOperation {
	private CellOperation cellOperation;
	private GameBean gameBean;
	private Long row;
	private Long column;
}
