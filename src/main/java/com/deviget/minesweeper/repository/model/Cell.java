package com.deviget.minesweeper.repository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.GenericGenerator;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Builder
@Table(name = "cells")
public class Cell {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;

	@Column(name = "`row_number`", nullable = false, length = 11)
	private Long row;

	@Column(name = "`col_number`", nullable = false, length = 11)
	private Long column;

	@Enumerated(EnumType.STRING)
	@Column(name = "cell_content", nullable = false, length = 10)
	private CellContent cellContent;

	@Column(length = 11)
	private Long minesAround;

	@Enumerated(EnumType.STRING)
	@Column(name = "cell_operation", nullable = false, length = 20)
	private CellOperation cellOperation;
}
