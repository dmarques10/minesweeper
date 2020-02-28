package com.deviget.minesweeper.mapper.impl;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.deviget.minesweeper.bean.GameBean;
import com.deviget.minesweeper.mapper.GameMapper;
import com.deviget.minesweeper.repository.model.Game;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

@Component
public class GameMapperImpl implements GameMapper {

	private MapperFacade facade;

	@PostConstruct
	public void init() {
		MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
		mapperFactory.classMap(Game.class, GameBean.class)
			.byDefault()
			.register();
		facade = mapperFactory.getMapperFacade();
	}

	@Override
	public GameBean mapToBean(Game game) {
		return facade.map(game, GameBean.class);
	}
}
