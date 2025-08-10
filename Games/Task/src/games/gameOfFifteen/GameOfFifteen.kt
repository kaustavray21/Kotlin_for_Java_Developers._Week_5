package games.gameOfFifteen

import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
    object : Game {
        private val board = createGameBoard<Int?>(4)

        override fun initialize() {
            val initialValues = initializer.initialPermutation
            board.getAllCells().forEachIndexed { index, cell ->
                if (index < initialValues.size) {
                    board[cell] = initialValues[index]
                } else {
                    board[cell] = null
                }
            }
        }

        override fun canMove(): Boolean = !hasWon()

        override fun hasWon(): Boolean {
            val currentValues = board.getAllCells().map { board[it] }
            val winningValues = (1..15).toList() + null
            return currentValues == winningValues
        }

        override fun processMove(direction: Direction) {
            if (!canMove()) return

            val emptyCell = board.find { it == null } ?: return

            val neighborCell = with(board) { emptyCell.getNeighbour(direction.reversed()) }

            if (neighborCell != null) {
                board[emptyCell] = board[neighborCell]
                board[neighborCell] = null
            }
        }

        override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }
    }


