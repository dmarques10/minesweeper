package com.deviget.minesweeper.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.deviget.minesweeper.repository.model.CellContent;
import com.deviget.minesweeper.repository.model.CellOperation;
import com.fasterxml.jackson.annotation.JsonInclude;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CellBean {

	private Long id;
	private Long row;
	private Long column;
	private CellContent cellContent;
	private Long minesAround;
	private CellOperation cellOperation;
}
