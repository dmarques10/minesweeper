package com.deviget.minesweeper.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardRequest {
	@NotEmpty
	private String userName;

	@NotNull
	@Min(2)
	@Max(50)
	private Long columns;

	@NotNull
	@Min(2)
	@Max(50)
	private Long rows;

	@NotNull
	@Min(1)
	@Max(2401)
	private Long mines;
}
