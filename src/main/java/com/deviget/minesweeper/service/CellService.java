package com.deviget.minesweeper.service;

import java.util.List;

import com.deviget.minesweeper.bean.CellBean;
import com.deviget.minesweeper.request.BoardRequest;
import com.deviget.minesweeper.request.GameOperation;

public interface CellService {
	List<CellBean> createCells(BoardRequest request);

	List<CellBean> operation(GameOperation gameOperation);
}
