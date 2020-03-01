package com.deviget.minesweeper.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.deviget.minesweeper.bean.GameBean;
import com.deviget.minesweeper.mother.BoardRequestMother;
import com.deviget.minesweeper.mother.GameBeanMother;
import com.deviget.minesweeper.mother.PlayRequestMother;
import com.deviget.minesweeper.repository.model.CellOperation;
import com.deviget.minesweeper.request.BoardRequest;
import com.deviget.minesweeper.request.PlayRequest;
import com.deviget.minesweeper.service.GameService;

public class GameControllerTest {
	@Mock
	private GameService gameService;

	private MineSweeperController mineSweeperController;

	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mineSweeperController = new MineSweeperController(gameService);
	}

	@Test
	public void testCreate() {
		BoardRequest request = BoardRequestMother.empty();
		GameBean expectedResponse = GameBeanMother.basic();
		when(gameService.createGame(request)).thenReturn(expectedResponse);

		ResponseEntity<GameBean> response = mineSweeperController.createGame(request);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody()).isEqualTo(expectedResponse);
		verify(gameService, times(1)).createGame(request);
		verifyNoMoreInteractions(gameService);
	}

	@Test
	public void testPerformOperation() {
		GameBean gameBean = GameBeanMother.basic();
		String userName = gameBean.getUserName();
		PlayRequest playRequest = PlayRequestMother.basic();
		CellOperation cellOperation = CellOperation.REVEALED;
		Long row = 1L;
		Long column = 1L;
		when(gameService.play(userName, playRequest)).thenReturn(gameBean);

		ResponseEntity<GameBean> response = mineSweeperController.playGame(playRequest, userName);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(gameBean);
		verify(gameService, times(1)).play(userName, playRequest);
		verifyNoMoreInteractions(gameService);
	}

	@Test
	public void testPause() {
		String userName = GameBeanMother.basic().getUserName();
		assertThat(mineSweeperController.pause(userName).getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void testResume() {
		String userName = GameBeanMother.basic().getUserName();
		assertThat(mineSweeperController.resume(userName).getStatusCode()).isEqualTo(HttpStatus.OK);
	}
}
