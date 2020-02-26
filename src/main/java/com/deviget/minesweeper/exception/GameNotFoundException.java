package com.deviget.minesweeper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class GameNotFoundException extends RuntimeException {
	private static final String ERROR_MESSAGE = "Game with username=%s not found";

	public GameNotFoundException(String userName) {
		super(String.format(ERROR_MESSAGE, userName));
	}
}
