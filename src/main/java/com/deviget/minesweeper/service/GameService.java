package com.deviget.minesweeper.service;

import com.deviget.minesweeper.bean.GameBean;
import com.deviget.minesweeper.request.BoardRequest;
import com.deviget.minesweeper.request.PlayRequest;

public interface GameService {
	GameBean createGame(BoardRequest request);

	GameBean play(String userName, PlayRequest request);

	void pause(String userName);

	void resume(String userName);
}
