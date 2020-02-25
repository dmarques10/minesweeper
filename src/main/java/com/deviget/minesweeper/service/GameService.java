package com.deviget.minesweeper.service;

import com.deviget.minesweeper.bean.GameBean;
import com.deviget.minesweeper.request.BoardRequest;

public interface GameService {
	GameBean createGame(BoardRequest request);
}
