package com.deviget.minesweeper.service.impl;

import static com.deviget.minesweeper.repository.model.GameStatus.ACTIVE;
import static com.deviget.minesweeper.repository.model.GameStatus.FINISHED_STATUS;
import static com.deviget.minesweeper.repository.model.GameStatus.PAUSE;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deviget.minesweeper.bean.CellBean;
import com.deviget.minesweeper.bean.GameBean;
import com.deviget.minesweeper.exception.GameNotFoundException;
import com.deviget.minesweeper.exception.MinesweeperException;
import com.deviget.minesweeper.mapper.CellMapper;
import com.deviget.minesweeper.mapper.GameMapper;
import com.deviget.minesweeper.repository.GameRepository;
import com.deviget.minesweeper.repository.model.Cell;
import com.deviget.minesweeper.repository.model.CellContent;
import com.deviget.minesweeper.repository.model.CellOperation;
import com.deviget.minesweeper.repository.model.Game;
import com.deviget.minesweeper.repository.model.GameStatus;
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
	@Transactional
	public GameBean createGame(BoardRequest request) {
		if (gameRepository.findByUserNameAndGameStatus(request.getUserName(), ACTIVE).isPresent()) {
			throw new MinesweeperException(String.format("[Minesweeper Service] - Already exists a game for username=%s", request.getUserName()));
		}
		List<CellBean> cellBeans = cellService.createCells(request);
		List<Cell> cells = cellService.save(cellBeans);
		Game game = Game.builder()
			.gameStatus(ACTIVE)
			.cells(cells)
			.columns(request.getColumns())
			.rows(request.getRows())
			.mines(request.getMines())
			.userName(request.getUserName())
			.build();
		return gameMapper.mapToBean(gameRepository.save(game));
	}

	//TODO: check response
	@Override
	@Transactional
	public GameBean play(String userName, PlayRequest request) {
		GameBean gameBean = gameRepository.findByUserNameAndGameStatus(userName, ACTIVE).map(game -> gameMapper.mapToBean(game))
			.orElseThrow(() -> new GameNotFoundException(userName));

		GameOperation gameOperation = GameOperation.builder()
			.cellOperation(request.getCellOperation())
			.gameBean(gameBean)
			.row(request.getRow())
			.column(request.getColumn())
			.build();

		try {

			List<CellBean> cellBeans = cellService.operation(gameOperation);
			if (gameIsFinished(userName)) {
				gameRepository.updateGameStatusByUserName(GameStatus.WON, userName);
				return gameMapper.mapToBean(gameRepository.findByUserName(userName).orElseThrow(() -> new GameNotFoundException(userName)));
			}

			//TODO: check response
			gameBean.setCells(cellBeans);
			return gameBean;
		} catch (MineExplodedException e) {

			gameRepository.updateGameStatusByUserName(GameStatus.LOST, userName);
			return gameMapper.mapToBean(gameRepository.findByUserName(userName).orElseThrow(() -> new GameNotFoundException(userName)));
		}
	}

	boolean gameIsFinished(String userName) {
		return gameRepository.findByUserName(userName)
			.map(Game::getCells)
			.orElseThrow(() -> new GameNotFoundException(userName))
			.stream()
			.noneMatch(cell -> isMineAndNotFlagged(cell) || isNumberAndNotRevealed(cell));
	}

	boolean isMineAndNotFlagged(Cell cell) {
		return CellContent.MINE.equals(cell.getCellContent()) && !CellOperation.FLAGGED.equals(cell.getCellOperation());
	}

	boolean isNumberAndNotRevealed(Cell cell) {
		return CellContent.NUMBER.equals(cell.getCellContent()) && !CellOperation.REVEALED.equals(cell.getCellOperation());
	}

	@Override
	@Transactional
	public void pause(String userName) {
		gameRepository.findByUserNameAndGameStatusNotIn(userName, FINISHED_STATUS)
			.orElseThrow(() -> new GameNotFoundException(userName));

		gameRepository.updateGameStatusByUserName(PAUSE, userName);
	}

	@Override
	@Transactional
	public void resume(String userName) {
		gameRepository.findByUserNameAndGameStatusNotIn(userName, FINISHED_STATUS)
			.orElseThrow(() -> new GameNotFoundException(userName));

		gameRepository.updateGameStatusByUserName(ACTIVE, userName);
	}
}
