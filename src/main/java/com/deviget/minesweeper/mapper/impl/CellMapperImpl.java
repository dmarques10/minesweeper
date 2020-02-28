package com.deviget.minesweeper.mapper.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.deviget.minesweeper.bean.CellBean;
import com.deviget.minesweeper.mapper.CellMapper;
import com.deviget.minesweeper.repository.model.Cell;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

@Component
public class CellMapperImpl implements CellMapper {
	private MapperFacade facade;

	@PostConstruct
	public void init() {
		MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
		mapperFactory.classMap(Cell.class, CellBean.class)
			.byDefault()
			.register();
		facade = mapperFactory.getMapperFacade();
	}

	@Override
	public List<Cell> mapToEntities(List<CellBean> cells) {
		return cells.stream().map(this::mapToEntity).collect(Collectors.toList());
	}

	@Override
	public Cell mapToEntity(CellBean cell) {
		return facade.map(cell, Cell.class);
	}
}
