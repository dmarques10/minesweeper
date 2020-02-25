package com.deviget.minesweeper.mapper;

import com.deviget.minesweeper.bean.GameBean;
import com.deviget.minesweeper.repository.model.Game;

public interface GameMapper {
	GameBean mapToBean(Game game);
}
