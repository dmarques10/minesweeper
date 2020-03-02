package com.deviget.minesweeper.service.impl;

import static com.deviget.minesweeper.utils.TestConstants.COLUMNS_QUANTITY;
import static com.deviget.minesweeper.utils.TestConstants.MINE_COLUMN;
import static com.deviget.minesweeper.utils.TestConstants.MINE_ROW;
import static com.deviget.minesweeper.utils.TestConstants.NUMBER_COLUMN;
import static com.deviget.minesweeper.utils.TestConstants.NUMBER_ROW;
import static com.deviget.minesweeper.utils.TestConstants.ROWS_QUANTITY;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.util.Lists;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.deviget.minesweeper.bean.CellBean;
import com.deviget.minesweeper.bean.GameBean;
import com.deviget.minesweeper.mapper.CellMapper;
import com.deviget.minesweeper.mother.BoardRequestMother;
import com.deviget.minesweeper.mother.CellBeanMother;
import com.deviget.minesweeper.mother.GameBeanMother;
import com.deviget.minesweeper.repository.CellRepository;
import com.deviget.minesweeper.repository.model.CellContent;
import com.deviget.minesweeper.repository.model.CellOperation;
import com.deviget.minesweeper.request.BoardRequest;
import com.deviget.minesweeper.request.GameOperation;
import com.deviget.minesweeper.utils.TestConstants;
import com.google.common.collect.Sets;


public class CellServiceImplTest {

	@Mock
	private CellRepository cellRepository;

	@Mock
	private CellMapper cellMapper;


	private CellServiceImpl cellService;

	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
		cellService = new CellServiceImpl(cellRepository, cellMapper);
	}

	@Test
	public void testGenerateRandomMines() {
		GameBean gameBean = GameBeanMother.basic();
		BoardRequest request = BoardRequestMother.basic();
		int totalCells = gameBean.getRows().intValue() * gameBean.getColumns().intValue();

		List<CellBean> cellBeans = cellService.createCells(request);

		AssertionsForClassTypes.assertThat(cellBeans.stream()
			.filter(cell -> CellContent.MINE.equals(cell.getCellContent()))
			.count()).isEqualTo(gameBean.getMines().intValue());
		AssertionsForClassTypes.assertThat(cellBeans.stream()
			.map(cell -> Pair.of(cell.getRow(), cell.getColumn()))
			.collect(Collectors.toSet())
			.size()).isEqualTo(totalCells);
	}

	@Test
	public void testPopulateWithMines() {
		List<CellBean> CellBeans = cellService.createMines(TestConstants.ROWS_QUANTITY, TestConstants.COLUMNS_QUANTITY, TestConstants.MINES_QUANTITY);

		AssertionsForClassTypes.assertThat(CellBeans.size()).isEqualTo(TestConstants.MINES_QUANTITY);
		Set<Pair<Long, Long>> rowColumnPairs = Sets.newHashSet();
		CellBeans.stream().forEach(CellBean -> {
			assertThat(CellBean.getCellContent()).isEqualTo(CellContent.MINE);
			rowColumnPairs.add(Pair.of(CellBean.getRow(), CellBean.getColumn()));
		});
		AssertionsForClassTypes.assertThat(rowColumnPairs.size()).isEqualTo(TestConstants.MINES_QUANTITY);
	}

	@Test
	public void testGetNewRandomPairInRange_createsPairAndFinishes() {
		Supplier<Long> rowSupplier = () -> ROWS_QUANTITY;
		Supplier<Long> columnSupplier = () -> COLUMNS_QUANTITY;
		BiPredicate<Long, Long> rowColumnVerifier = (row, column) -> true;
		Pair<Long, Long> expectedPair = Pair.of(ROWS_QUANTITY, COLUMNS_QUANTITY);

		assertThat(cellService.getNewRandomPairInRange(rowSupplier, columnSupplier, rowColumnVerifier)).isEqualTo(expectedPair);
	}

	@Test
	public void testGetNewRandomPairInRange_ifCreatesAlreadyExistingPair_createsAnotherOne() {
		Long firstRow = 1L;
		Long firstColumn = 1L;
		Long secondRow = 2L;
		Long secondColumn = 2L;
		Supplier<Long> rowSupplier = Mockito.mock(Supplier.class);
		Supplier<Long> columnSupplier = Mockito.mock(Supplier.class);
		BiPredicate<Long, Long> rowColumnVerifier = Mockito.mock(BiPredicate.class);
		Pair<Long, Long> expectedPair = Pair.of(secondRow, secondColumn);
		when(rowSupplier.get()).thenReturn(firstRow).thenReturn(secondRow);
		when(columnSupplier.get()).thenReturn(firstColumn).thenReturn(secondColumn);
		when(rowColumnVerifier.test(firstRow, firstColumn)).thenReturn(false);
		when(rowColumnVerifier.test(secondRow, secondColumn)).thenReturn(true);

		assertThat(cellService.getNewRandomPairInRange(rowSupplier, columnSupplier, rowColumnVerifier)).isEqualTo(expectedPair);
		verify(rowSupplier, times(2)).get();
		verify(columnSupplier, times(2)).get();
		verify(rowColumnVerifier, times(1)).test(firstRow, firstColumn);
		verify(rowColumnVerifier, times(1)).test(secondRow, secondColumn);
		verifyNoMoreInteractions(rowSupplier);
		verifyNoMoreInteractions(columnSupplier);
		verifyNoMoreInteractions(rowColumnVerifier);
	}

	@Test
	public void testMapToCellBean() {
		Pair<Long, Long> pair = Pair.of(MINE_ROW, MINE_COLUMN);
		CellBean cellBean = CellBeanMother.mine();

		AssertionsForClassTypes.assertThat(cellService.mapToCellBean(pair)).isEqualTo(cellBean);
	}

	@Test
	public void testPopulateWithNumbers() {
		List<CellBean> mines = Lists.newArrayList(CellBeanMother.mine());
		Long rows = ROWS_QUANTITY;
		Long columns = COLUMNS_QUANTITY;
		Long totalCells = rows * columns;
		Long minesQuantity = (long) mines.size();

		List<CellBean> result = cellService.addNumbers(mines, rows, columns);

		AssertionsForClassTypes.assertThat(result.stream()
			.filter(cell -> CellContent.MINE.equals(cell.getCellContent()))
			.count()).isEqualTo(minesQuantity);
		AssertionsForClassTypes.assertThat(result.stream()
			.map(cell -> Pair.of(cell.getRow(), cell.getColumn()))
			.collect(Collectors.toSet())
			.size()).isEqualTo(totalCells.intValue());
	}

	@Test
	public void testPerformOperationREVEALED_onNumber_doesntThrowException() {
		CellBean number = CellBeanMother.number();
		CellBean mine = CellBeanMother.mine();
		GameBean gameBean = GameBeanMother.basic();
		gameBean.setCells(Lists.newArrayList(number, mine));
		Long row = number.getRow();
		Long column = number.getColumn();
		GameOperation gameOperation = GameOperation.builder()
			.gameBean(gameBean)
			.row(row)
			.column(column)
			.cellOperation(CellOperation.REVEALED)
			.build();

		assertThatCode(() -> cellService.operation(gameOperation))
			.doesNotThrowAnyException();
	}

	@Test
	public void testQuestionMarkedOperationFunction() {
		CellOperation toUpdate = CellOperation.QUESTION_MARKED;
		CellBean number = CellBeanMother.number();
		GameBean gameBean = GameBeanMother.basic();
		gameBean.setCells(Lists.newArrayList(number));
		GameOperation gameCellOperation = GameOperation.builder()
			.row(number.getRow())
			.column(number.getColumn())
			.gameBean(gameBean)
			.cellOperation(CellOperation.REVEALED)
			.build();
		final CellBean cellBean = CellBeanMother.number();
		cellBean.setCellOperation(toUpdate);
		List<CellBean> expected = Lists.newArrayList(cellBean);

		Function<GameOperation, List<CellBean>> function = cellService.questionMarkedFunction();
		assertThat(function.apply(gameCellOperation)).isEqualTo(expected);
	}

	@Test
	public void testGetCellFromPosition() {
		CellBean number = CellBeanMother.number();
		CellBean mine = CellBeanMother.mine();
		GameBean gameBean = GameBeanMother.basic();
		gameBean.setCells(Lists.newArrayList(number, mine));

		AssertionsForClassTypes.assertThat(cellService.getCellFromPosition(gameBean, number.getRow(), number.getColumn())).contains(number);
	}

	@Test
	public void testGetCellFromPosition_whenOutOfBounds_doesntIterate() {
		GameBean gameBean = GameBeanMother.basic();

		AssertionsForClassTypes.assertThat(cellService.getCellFromPosition(gameBean, gameBean.getRows() - 1, gameBean.getColumns() + 1)).isEmpty();
	}

	@Test
	public void testBuildPossibleDirections() {
		Long row = NUMBER_ROW;
		Long column = NUMBER_COLUMN;

		List<Pair<Long, Long>> directions = cellService.buildPossibleDirections(row, column);

		assertThat(directions).hasSize(8);
		assertThat(directions).containsExactly(
			Pair.of(row - 1, column - 1),
			Pair.of(row - 1, column),
			Pair.of(row - 1, column + 1),
			Pair.of(row, column - 1),
			Pair.of(row, column + 1),
			Pair.of(row + 1, column - 1),
			Pair.of(row + 1, column),
			Pair.of(row + 1, column + 1));
	}

	@Test
	public void testHasMineInPosition_whenItsNotMine_returnFalse() {
		CellBean cell = CellBeanMother.number();
		GameBean gameBean = GameBeanMother.basic();
		gameBean.setCells(Lists.newArrayList(cell));
		Long row = 1L;
		Long column = 1L;
		Pair<Long, Long> pair = Pair.of(row, column);

		AssertionsForClassTypes.assertThat(cellService.hasMineInPosition(gameBean, pair)).isFalse();

	}

	@Test
	public void testHasMineInPosition_whenItsOutsideOfBounds_returnFalse() {
		GameBean gameBean = GameBeanMother.basic();
		Long row = 0L;
		Long column = 0L;
		Pair<Long, Long> pair = Pair.of(row, column);

		AssertionsForClassTypes.assertThat(cellService.hasMineInPosition(gameBean, pair)).isFalse();
	}

	@Test
	public void testIsOutOfBounds_isTrue() {
		GameBean gameBean = GameBeanMother.basic();
		List<Pair<Long, Long>> limits = Arrays.asList(
			Pair.of(1L, 0L),
			Pair.of(0L, 0L),
			Pair.of(0L, 1L),
			Pair.of(0L, gameBean.getColumns()),
			Pair.of(0L, gameBean.getColumns() + 1),
			Pair.of(1L, gameBean.getColumns() + 1),
			Pair.of(gameBean.getRows(), gameBean.getColumns() + 1),
			Pair.of(gameBean.getRows() + 1, gameBean.getColumns() + 1),
			Pair.of(gameBean.getRows() + 1, gameBean.getColumns()),
			Pair.of(gameBean.getRows() + 1, 1L),
			Pair.of(gameBean.getRows() + 1, 0L),
			Pair.of(gameBean.getRows(), 0L));

		limits.forEach(pair -> AssertionsForClassTypes.assertThat(cellService.isOutOfBounds(gameBean, pair.getLeft(), pair.getRight())).isTrue());
	}

	@Test
	public void testIsOutOfBounds_isFalse() {
		GameBean gameBean = GameBeanMother.basic();
		List<Pair<Long, Long>> limits = Arrays.asList(
			Pair.of(1L, 1L),
			Pair.of(1L, gameBean.getColumns()),
			Pair.of(gameBean.getRows(), gameBean.getColumns()),
			Pair.of(gameBean.getRows(), 1L));

		limits.forEach(pair -> AssertionsForClassTypes.assertThat(cellService.isOutOfBounds(gameBean, pair.getLeft(), pair.getRight())).isFalse());
	}
}
