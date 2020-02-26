package com.deviget.minesweeper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.deviget.minesweeper.repository.model.CellOperation;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class GameOperationNotSupportedException extends RuntimeException {
	private static final String ERROR_MESSAGE = "Invalid Cell Operation=%s";

	public GameOperationNotSupportedException(CellOperation cellOperation) {
		super(String.format(ERROR_MESSAGE, cellOperation));
	}
}
