package com.deviget.minesweeper.mother;

import static com.deviget.minesweeper.utils.TestConstants.MINE_COLUMN;
import static com.deviget.minesweeper.utils.TestConstants.MINE_ROW;

import com.deviget.minesweeper.bean.CellBean;
import com.deviget.minesweeper.repository.model.CellContent;
import com.deviget.minesweeper.repository.model.CellOperation;

public class CellBeanMother {
	public static CellBean mine() {
		return CellBean.builder()
			.row(MINE_ROW)
			.column(MINE_COLUMN)
			.cellContent(CellContent.MINE)
			.cellOperation(CellOperation.NONE)
			.build();
	}
}
