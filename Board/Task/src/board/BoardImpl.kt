package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)

open class SquareBoardImpl(override val width: Int) : SquareBoard {

    private val cells: Array<Array<Cell>> =
        Array(width) { i -> Array(width) { j -> Cell(i + 1, j + 1) } }

    override fun getCellOrNull(i: Int, j: Int): Cell? =
        if (i in 1..width && j in 1..width) cells[i - 1][j - 1] else null

    override fun getCell(i: Int, j: Int): Cell =
        getCellOrNull(i, j) ?: throw IllegalArgumentException("Cell ($i, $j) is out of bounds")

    override fun getAllCells(): Collection<Cell> =
        cells.flatten()

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> =
        jRange.mapNotNull { j -> getCellOrNull(i, j) }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> =
        iRange.mapNotNull { i -> getCellOrNull(i, j) }

    override fun Cell.getNeighbour(direction: Direction): Cell? = when (direction) {
        Direction.UP    -> getCellOrNull(i - 1, j)
        Direction.DOWN  -> getCellOrNull(i + 1, j)
        Direction.LEFT  -> getCellOrNull(i, j - 1)
        Direction.RIGHT -> getCellOrNull(i, j + 1)
    }
}

class GameBoardImpl<T>(width: Int) : SquareBoardImpl(width), GameBoard<T> {

    private val values: MutableMap<Cell, T?> = getAllCells().associateWith { null }.toMutableMap()

    override fun get(cell: Cell): T? = values[cell]
    override fun set(cell: Cell, value: T?) { values[cell] = value }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> =
        values.entries.filter { predicate(it.value) }.map { it.key }

    override fun find(predicate: (T?) -> Boolean): Cell? =
        values.entries.find { predicate(it.value) }?.key

    override fun any(predicate: (T?) -> Boolean): Boolean =
        values.values.any(predicate)

    override fun all(predicate: (T?) -> Boolean): Boolean =
        values.values.all(predicate)
}