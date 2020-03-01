package com.deviget.minesweeper.mother;

import com.deviget.minesweeper.repository.model.CellOperation;
import com.deviget.minesweeper.request.PlayRequest;

public class PlayRequestMother {
	public static PlayRequest basic() {
		return PlayRequest.builder()
			.cellOperation(CellOperation.REVEALED)
			.column(1L)
			.row(1L)
			.build();
	}
}
