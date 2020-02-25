package com.deviget.minesweeper.mapper;

import java.util.List;

import com.deviget.minesweeper.bean.CellBean;
import com.deviget.minesweeper.repository.model.Cell;

public interface CellMapper {
	List<Cell> mapToEntities(List<CellBean> cells);
}
