package com.deviget.minesweeper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MinesweeperException extends RuntimeException {
	public MinesweeperException(String message) {
		super(message);
	}
}
