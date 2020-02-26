package com.deviget.minesweeper.repository.model;

import java.util.EnumSet;

public enum GameStatus {
	ACTIVE, PAUSE, WON, LOST;

	public static EnumSet<GameStatus> FINISHED_STATUS = EnumSet.of(WON, LOST);
}
