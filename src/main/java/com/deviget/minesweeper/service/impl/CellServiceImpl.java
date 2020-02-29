package com.deviget.minesweeper.service.impl;

import static com.deviget.minesweeper.repository.model.CellOperation.FLAGGED;
import static com.deviget.minesweeper.repository.model.CellOperation.QUESTION_MARKED;
import static com.deviget.minesweeper.repository.model.CellOperation.REVEALED;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deviget.minesweeper.bean.CellBean;
import com.deviget.minesweeper.bean.GameBean;
import com.deviget.minesweeper.exception.GameOperationNotSupportedException;
import com.deviget.minesweeper.exception.InvalidPositionException;
import com.deviget.minesweeper.mapper.CellMapper;
import com.deviget.minesweeper.repository.CellRepository;
import com.deviget.minesweeper.repository.model.Cell;
import com.deviget.minesweeper.repository.model.CellContent;
import com.deviget.minesweeper.repository.model.CellOperation;
import com.deviget.minesweeper.request.BoardRequest;
import com.deviget.minesweeper.request.GameOperation;
import com.deviget.minesweeper.service.CellService;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Service
@RequiredArgsConstructor
public class CellServiceImpl implements CellService {
	private final CellRepository cellRepository;
	private final CellMapper cellMapper;
	private final Map<CellOperation, Function<GameOperation, List<CellBean>>> cellOperationFunction = ImmutableMap.of(
		REVEALED, revealedOperationFunction(),
		FLAGGED, flaggedOperationFunction(),
		QUESTION_MARKED, questionMarkedFunction());

	@Override
	public List<CellBean> createCells(BoardRequest request) {
		long rows = request.getRows();
		long columns = request.getColumns();
		long minesQuantity = request.getMines();

		List<CellBean> cells = createMines(rows, columns, minesQuantity);
		return addNumbers(cells, rows, columns);
	}

	@Override
	public List<CellBean> operation(GameOperation gameOperation) {
		return cellOperationFunction.entrySet().stream()
			.filter(entry -> entry.getKey().equals(gameOperation.getCellOperation()))
			.findFirst()
			.orElseThrow(() -> new GameOperationNotSupportedException(gameOperation.getCellOperation()))
			.getValue()
			.apply(gameOperation);
	}

	@Override
	@Transactional
	public List<Cell> save(List<CellBean> cellBeans) {
		final List<Cell> cells = cellMapper.mapToEntities(cellBeans);
		return cellRepository.saveAll(cells);
	}

	private List<CellBean> addNumbers(List<CellBean> cells, long rows, long columns) {
		List<CellBean> alreadyCalculatedMines = Lists.newArrayList();
		for (long i = 1; i <= rows; i++) {
			for (long j = 1; j <= columns; j++) {
				long currentRow = i;
				long currentColumn = j;
				if (alreadyCalculatedMines.stream().noneMatch(cell -> (cell.getRow() == currentRow) && (cell.getColumn() == currentColumn))) {
					// If it wasn't part of the cells, then it's not a mine, we create a number
					cells.add(CellBean.builder()
						.row(i)
						.column(j)
						.cellOperation(CellOperation.NONE)
						.cellContent(CellContent.NUMBER)
						.build());
				}
			}
		}
		return cells;
	}

	private List<CellBean> createMines(long rows, long columns, long minesQuantity) {
		List<Pair<Long, Long>> alreadyOccupiedCells = Lists.newArrayList();

		LongStream.range(1, minesQuantity + 1).forEach(i -> {

			// Generate a new pair inside the row and cols and verifying that it's not already occupied
			Pair<Long, Long> newPair = getNewRandomPairInRange(
				() -> ThreadLocalRandom.current().nextLong(1, rows + 1),
				() -> ThreadLocalRandom.current().nextLong(1, columns + 1),
				(row, col) -> alreadyOccupiedCells.stream().noneMatch(pair -> pair.compareTo(Pair.of(row, col)) == 0)
			);
			alreadyOccupiedCells.add(newPair);
		});

		return alreadyOccupiedCells.stream()
			.map(this::mapToCellBean)
			.collect(Collectors.toList());
	}

	Pair<Long, Long> getNewRandomPairInRange(Supplier<Long> rowSupplier,
																					 Supplier<Long> columnSupplier,
																					 BiPredicate<Long, Long> rowColumnVerifier) {
		long randomRow = 0;
		long randomColumn = 0;
		boolean finished = false;
		while (!finished) {
			randomRow = rowSupplier.get();
			randomColumn = columnSupplier.get();
			if (rowColumnVerifier.test(randomRow, randomColumn)) {
				finished = true;
			}
		}
		return Pair.of(randomRow, randomColumn);
	}

	CellBean mapToCellBean(Pair<Long, Long> pair) {
		return CellBean.builder()
			.row(pair.getLeft())
			.column(pair.getRight())
			.cellContent(CellContent.MINE)
			.cellOperation(CellOperation.NONE)
			.build();
	}

	Function<GameOperation, List<CellBean>> revealedOperationFunction() {
		return gameOperation -> obtainCellAndApplyFunction(
			gameOperation,
			cell -> {
				if (isMine(cell)) {
					cellRepository.updateCellOperationById(REVEALED, cell.getId());
					throw new MineExplodedException(gameOperation.getRow(), gameOperation.getColumn(), gameOperation.getGameBean());
				}

				List<CellBean> updatedCells = Lists.newArrayList(populateMinesAround(cell, gameOperation.getGameBean()));
				updatedCells.forEach(updatedCell -> cellRepository.updateCellOperationAndMinesAroundById(REVEALED, updatedCell.getMinesAround(), updatedCell.getId()));
				return updatedCells;
			});
	}

	Function<GameOperation, List<CellBean>> flaggedOperationFunction() {
		return gameOperation -> obtainCellAndApplyFunction(
			gameOperation,
			cell -> {
				cellRepository.updateCellOperationById(FLAGGED, cell.getId());
				cell.setCellOperation(FLAGGED);
				return Arrays.asList(cell);
			});
	}

	Function<GameOperation, List<CellBean>> questionMarkedFunction() {
		return gameOperation -> obtainCellAndApplyFunction(
			gameOperation,
			cell -> {
				cellRepository.updateCellOperationById(QUESTION_MARKED, cell.getId());
				cell.setCellOperation(QUESTION_MARKED);
				return Arrays.asList(cell);
			});
	}

	List<CellBean> obtainCellAndApplyFunction(GameOperation gameOperation,
																						Function<CellBean, List<CellBean>> function) {
		Long row = gameOperation.getRow();
		Long column = gameOperation.getColumn();
		GameBean gameBean = gameOperation.getGameBean();

		// We update the status from it in the DB
		CellBean cell = getCellFromPosition(gameBean, row, column)
			.orElseThrow(() -> new InvalidPositionException(row, column));

		if (CellOperation.REVEALED.equals(cell.getCellOperation())) {
			// Cell is already revealed, nothing to do
			return Lists.newArrayList();
		}

		return function.apply(cell);
	}

	Optional<CellBean> getCellFromPosition(GameBean gameBean, Long row, Long column) {
		if (isOutOfBounds(gameBean, row, column)) {
			return Optional.empty();
		}
		return gameBean.getCells().parallelStream()
			.filter(cell -> hasPosition(cell, row, column))
			.findFirst();
	}

	boolean isOutOfBounds(GameBean gameBean, long row, long column) {
		return ((row < 1) ||
			(row > gameBean.getRows()) ||
			(column < 1) ||
			(column > gameBean.getColumns()));
	}

	boolean hasPosition(CellBean cellBean, Long row, Long column) {
		return row.longValue() == cellBean.getRow().longValue() &&
			column.longValue() == cellBean.getColumn().longValue();
	}

	Set<CellBean> populateMinesAround(CellBean cellBean, GameBean gameBean) {
		long row = cellBean.getRow();
		long column = cellBean.getColumn();

		// Check every possible direction (8 in total) and counting how many mines there are
		Stream<Boolean> results = buildPossibleDirections(row, column).stream()
			.map(pair -> hasMineInPosition(gameBean, pair));
		long minesAround = results
			.filter(Boolean::booleanValue)
			.count();

		cellBean.setCellOperation(CellOperation.REVEALED);
		cellBean.setMinesAround(minesAround);

		Set<CellBean> result = Sets.newHashSet(cellBean);
		if (cellBean.getMinesAround().intValue() == 0) {
			Set<CellBean> cellBeans = buildCellsAround(cellBean, gameBean)
				.filter(c -> CellOperation.NONE.equals(c.getCellOperation()))
				.map(c -> populateMinesAround(c, gameBean))
				.flatMap(Collection::stream)
				.collect(Collectors.toSet());
			result.addAll(cellBeans);
		}

		return result;
	}

	Stream<CellBean> buildCellsAround(CellBean cellBean, GameBean gameBean) {
		return buildPossibleDirections(cellBean.getRow(), cellBean.getColumn())
			.stream()
			.map(direction -> getCellFromPosition(gameBean, direction.getLeft(), direction.getRight()))
			.flatMap(Optional::stream);
	}

	boolean hasMineInPosition(GameBean gameBean, Pair<Long, Long> rowColumnPair) {
		if (isOutOfBounds(gameBean, rowColumnPair.getLeft(), rowColumnPair.getRight())) {
			return false;
		}

		return gameBean.getCells().stream().anyMatch(cellBean ->
			isMine(cellBean) &&
				hasPosition(cellBean, rowColumnPair.getLeft(), rowColumnPair.getRight()));
	}

	List<Pair<Long, Long>> buildPossibleDirections(Long row, Long column) {
		return Arrays.asList(Pair.of(row - 1, column - 1),
			Pair.of(row - 1, column),
			Pair.of(row - 1, column + 1),
			Pair.of(row, column - 1),
			Pair.of(row, column + 1),
			Pair.of(row + 1, column - 1),
			Pair.of(row + 1, column),
			Pair.of(row + 1, column + 1));
	}

	boolean isMine(CellBean cellBean) {
		return CellContent.MINE.equals(cellBean.getCellContent());
	}
}
