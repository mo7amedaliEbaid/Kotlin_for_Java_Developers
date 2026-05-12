package games.game2048

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Your task is to implement the game 2048 https://en.wikipedia.org/wiki/2048_(video_game).
 * Implement the utility methods below.
 *
 * After implementing it you can try to play the game running 'PlayGame2048'.
 */
fun newGame2048(initializer: Game2048Initializer<Int> = RandomGame2048Initializer): Game =
        Game2048(initializer)

class Game2048(private val initializer: Game2048Initializer<Int>) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        repeat(2) {
            board.addNewValue(initializer)
        }
    }

    override fun canMove() = board.any { it == null }

    override fun hasWon() = board.any { it == 2048 }

    override fun processMove(direction: Direction) {
        if (board.moveValues(direction)) {
            board.addNewValue(initializer)
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }
}

/*
 * Add a new value produced by 'initializer' to a specified cell in a board.
 */
fun GameBoard<Int?>.addNewValue(initializer: Game2048Initializer<Int>) {
    initializer.nextValue(this)?.let { (cell, value) -> this[cell] = value }
}

fun GameBoard<Int?>.moveValuesInRowOrColumn(rowOrColumn: List<Cell>): Boolean {
    val old = rowOrColumn.map { this[it] }
    val merged = old.moveAndMergeEqual { it * 2 }
    val new = merged + List(rowOrColumn.size - merged.size) { null }
    if (old == new) return false
    rowOrColumn.forEachIndexed { idx, cell -> this[cell] = new[idx] }
    return true
}

fun GameBoard<Int?>.moveValues(direction: Direction): Boolean {
    val range = 1..width
    return when (direction) {
        Direction.LEFT  -> range.map { i -> moveValuesInRowOrColumn(getRow(i, range)) }
        Direction.RIGHT -> range.map { i -> moveValuesInRowOrColumn(getRow(i, range.reversed())) }
        Direction.UP    -> range.map { j -> moveValuesInRowOrColumn(getColumn(range, j)) }
        Direction.DOWN  -> range.map { j -> moveValuesInRowOrColumn(getColumn(range.reversed(), j)) }
    }.any { it }
}