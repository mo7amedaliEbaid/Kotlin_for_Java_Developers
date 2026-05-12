package games.gameOfFifteen

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
    GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {

    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        val values = initializer.initialPermutation
        board.getAllCells().sortedWith(compareBy({ it.i }, { it.j })).forEachIndexed { idx, cell ->
            board[cell] = values.getOrNull(idx)
        }
    }

    override fun canMove(): Boolean = true

    override fun hasWon(): Boolean {
        val cells = board.getAllCells().sortedWith(compareBy({ it.i }, { it.j }))
        val values = cells.map { board[it] }
        return values == (1..15).toList() + listOf(null)
    }

    override fun processMove(direction: Direction) {
        val emptyCell = board.find { it == null } ?: return
        with(board) {
            val neighbour = emptyCell.getNeighbour(direction.reversed()) ?: return
            board[emptyCell] = board[neighbour]
            board[neighbour] = null
        }
    }

    override fun get(i: Int, j: Int): Int? = board[board.getCell(i, j)]
}
