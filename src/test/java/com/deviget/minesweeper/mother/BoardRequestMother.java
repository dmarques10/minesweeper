package com.deviget.minesweeper.mother;

import com.deviget.minesweeper.request.BoardRequest;

public class BoardRequestMother {
	public static BoardRequest empty() {
		return BoardRequest.builder().build();
	}
}
