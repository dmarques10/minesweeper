package com.deviget.minesweeper.service.impl;

import com.deviget.minesweeper.bean.GameBean;

public class MineExplodedException extends RuntimeException {
	public MineExplodedException(Long row, Long column, GameBean gameBean) {
	}
}
