package com.deviget.minesweeper.request;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

import com.deviget.minesweeper.repository.model.CellOperation;

@Data
@Builder
public class PlayRequest {
	@NotNull
	private Long column;

	@NotNull
	private Long row;

	@NotNull
	private CellOperation cellOperation;
}
