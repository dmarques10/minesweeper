package com.deviget.minesweeper.mother;

import static com.deviget.minesweeper.utils.TestConstants.COLUMNS_QUANTITY;
import static com.deviget.minesweeper.utils.TestConstants.MINES_QUANTITY;
import static com.deviget.minesweeper.utils.TestConstants.ROWS_QUANTITY;
import static com.deviget.minesweeper.utils.TestConstants.USER_NAME;

import com.deviget.minesweeper.request.BoardRequest;

public class BoardRequestMother {
	public static BoardRequest empty() {
		return BoardRequest.builder().build();
	}

	public static BoardRequest basic() {
		return BoardRequest.builder()
			.columns(COLUMNS_QUANTITY)
			.rows(ROWS_QUANTITY)
			.mines(MINES_QUANTITY)
			.userName(USER_NAME)
			.build();
	}
}
