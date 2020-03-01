package com.deviget.minesweeper.mother;

import static com.deviget.minesweeper.utils.TestConstants.COLUMNS_QUANTITY;
import static com.deviget.minesweeper.utils.TestConstants.ID;
import static com.deviget.minesweeper.utils.TestConstants.MINES_QUANTITY;
import static com.deviget.minesweeper.utils.TestConstants.ROWS_QUANTITY;

import com.deviget.minesweeper.bean.GameBean;
import com.google.common.collect.Lists;

public class GameBeanMother {
	public static GameBean basic() {
		return GameBean.builder()
			.id(ID)
			.rows(ROWS_QUANTITY)
			.columns(COLUMNS_QUANTITY)
			.mines(MINES_QUANTITY)
			.cells(Lists.newArrayList(CellBeanMother.mine()))
			.build();
	}
}
