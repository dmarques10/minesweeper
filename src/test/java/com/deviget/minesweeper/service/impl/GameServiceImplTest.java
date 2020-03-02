package com.deviget.minesweeper.service.impl;

import static com.deviget.minesweeper.repository.model.GameStatus.FINISHED_STATUS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.deviget.minesweeper.bean.CellBean;
import com.deviget.minesweeper.bean.GameBean;
import com.deviget.minesweeper.exception.GameNotFoundException;
import com.deviget.minesweeper.mapper.GameMapper;
import com.deviget.minesweeper.mother.BoardRequestMother;
import com.deviget.minesweeper.mother.CellBeanMother;
import com.deviget.minesweeper.mother.GameBeanMother;
import com.deviget.minesweeper.repository.GameRepository;
import com.deviget.minesweeper.repository.model.Game;
import com.deviget.minesweeper.request.BoardRequest;
import com.deviget.minesweeper.service.CellService;
import com.deviget.minesweeper.utils.TestConstants;

public class GameServiceImplTest {
	@Mock
	private  GameRepository gameRepository;
	@Mock
	private  CellService cellService;
	@Mock
	private  GameMapper gameMapper;

	private GameServiceImpl gameService;

	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
		gameService = new GameServiceImpl(gameRepository, cellService, gameMapper);
	}

	@Test
	public void testCreate() {
		BoardRequest boardRequest = BoardRequestMother.empty();
		List<CellBean> cellBeans = Lists.newArrayList(CellBeanMother.mine());
		when(cellService.createCells(boardRequest)).thenReturn(cellBeans);

		assertThat(gameService.createGame(boardRequest)).isEqualTo(null);
	}

	@Test
	public void testPause() {
		GameBean gameBean = GameBeanMother.basic();
		String userName = gameBean.getUserName();
		when(gameRepository.findByUserNameAndGameStatusNotIn(userName,FINISHED_STATUS)).thenReturn(Optional.of(Game.builder().build()));

		assertThatCode(() -> gameService.pause(userName)).doesNotThrowAnyException();
	}

	@Test
	public void testPause_whenGameIsFinishedOrNotFound_throwsGameNotFoundException() {
		GameBean gameBean = GameBeanMother.basic();
		String userName = gameBean.getUserName();

		assertThatExceptionOfType(GameNotFoundException.class)
				.isThrownBy(() -> gameService.pause(userName))
				.withMessage("Game with username=%s not found", userName);
	}

	@Test
	public void testResume() {
		GameBean gameBean = GameBeanMother.basic();
		String userName = gameBean.getUserName();
		when(gameRepository.findByUserNameAndGameStatusNotIn(userName,FINISHED_STATUS)).thenReturn(Optional.of(Game.builder().build()));

		assertThatCode(() -> gameService.resume(userName)).doesNotThrowAnyException();
	}

	@Test
	public void testResume_whenGameIsFinishedOrNotFound_throwsGameNotFoundException() {
		GameBean gameBean = GameBeanMother.basic();
		String userName = gameBean.getUserName();

		assertThatExceptionOfType(GameNotFoundException.class)
				.isThrownBy(() -> gameService.resume(userName))
				.withMessage("Game with username=%s not found", userName);
	}


	@Test
	public void testGameIsFinished_whenNoGameIsFound_throwsGameNotFoundException() {
		String userName = TestConstants.USER_NAME;

		assertThatExceptionOfType(GameNotFoundException.class)
				.isThrownBy(() -> gameService.gameIsFinished(userName))
				.withMessage("Game with username=%s not found", userName);
	}




}
