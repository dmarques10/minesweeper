package com.deviget.minesweeper.mother;

import static com.deviget.minesweeper.utils.TestConstants.MINES_AROUND;
import static com.deviget.minesweeper.utils.TestConstants.MINE_COLUMN;
import static com.deviget.minesweeper.utils.TestConstants.MINE_ROW;
import static com.deviget.minesweeper.utils.TestConstants.NUMBER_COLUMN;
import static com.deviget.minesweeper.utils.TestConstants.NUMBER_ROW;

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

	public static CellBean number() {
		return CellBean.builder()
			.row(NUMBER_ROW)
			.column(NUMBER_COLUMN)
			.cellContent(CellContent.NUMBER)
			.minesAround(MINES_AROUND)
			.cellOperation(CellOperation.NONE)
			.build();
	}
}
