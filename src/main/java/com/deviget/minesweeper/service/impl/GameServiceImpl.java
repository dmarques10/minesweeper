package com.deviget.minesweeper.service.impl;

import static com.deviget.minesweeper.repository.model.GameStatus.ACTIVE;
import static com.deviget.minesweeper.repository.model.GameStatus.FINISHED_STATUS;
import static com.deviget.minesweeper.repository.model.GameStatus.PAUSE;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.deviget.minesweeper.bean.CellBean;
import com.deviget.minesweeper.bean.GameBean;
import com.deviget.minesweeper.exception.GameNotFoundException;
import com.deviget.minesweeper.exception.MinesweeperException;
import com.deviget.minesweeper.mapper.CellMapper;
import com.deviget.minesweeper.mapper.GameMapper;
import com.deviget.minesweeper.repository.GameRepository;
import com.deviget.minesweeper.repository.model.Game;
import com.deviget.minesweeper.request.BoardRequest;
import com.deviget.minesweeper.request.GameOperation;
import com.deviget.minesweeper.request.PlayRequest;
import com.deviget.minesweeper.service.CellService;
import com.deviget.minesweeper.service.GameService;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

	private final GameRepository gameRepository;
	private final CellService cellService;
	private final CellMapper cellMapper;
	private final GameMapper gameMapper;

	@Override
	public GameBean createGame(BoardRequest request) {
		if (gameRepository.findByUserNameAndGameStatus(request.getUserName(), ACTIVE).isPresent()) {
			throw new MinesweeperException(String.format("[Minesweeper Service] - Already exists a game for username=%s", request.getUserName()));
		}
		List<CellBean> cells = cellService.createCells(request);
		Game game = Game.builder()
			.gameStatus(ACTIVE)
			.cells(cellMapper.mapToEntities(cells))
			.columns(request.getColumns())
			.rows(request.getRows())
			.mines(request.getMines())
			.userName(request.getUserName())
			.build();
		return gameMapper.mapToBean(gameRepository.save(game));
	}

	@Override
	public GameBean play(String userName, PlayRequest request) {
		GameBean gameBean = gameRepository.findByUserNameAndGameStatus(userName, ACTIVE).map(game -> gameMapper.mapToBean(game))
			.orElseThrow(() -> new GameNotFoundException(userName));

		GameOperation gameOperation = GameOperation.builder()
			.cellOperation(request.getCellOperation())
			.gameBean(gameBean)
			.row(request.getRow())
			.column(request.getColumn())
			.build();


		List<CellBean> cellBeans = cellService.operation(gameOperation);
		//TODO: check if is finish
		return null;
	}

	@Override
	public void pause(String userName) {
		gameRepository.findByUserNameAndGameStatusNotIn(userName, FINISHED_STATUS)
			.orElseThrow(() -> new GameNotFoundException(userName));

		gameRepository.updateGameStatusByUserName(PAUSE, userName);
	}

	@Override
	public void resume(String userName) {
		gameRepository.findByUserNameAndGameStatusNotIn(userName, FINISHED_STATUS)
			.orElseThrow(() -> new GameNotFoundException(userName));

		gameRepository.updateGameStatusByUserName(ACTIVE, userName);
	}
}
