package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard = object : SquareBoard {
    override val width: Int = width
    private val cells: Map<Pair<Int, Int>, Cell>

    init {
        val cellList = (1..width).flatMap { i ->
            (1..width).map { j -> Cell(i, j) }
        }
        cells = cellList.associateBy { it.i to it.j }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? = cells[i to j]

    override fun getCell(i: Int, j: Int): Cell =
        getCellOrNull(i, j) ?: throw IllegalArgumentException("Cell ($i, $j) is out of bounds for a board of width $width")

    override fun getAllCells(): Collection<Cell> = cells.values

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        return jRange.mapNotNull { j -> getCellOrNull(i, j) }
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        return iRange.mapNotNull { i -> getCellOrNull(i, j) }
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? = when (direction) {
        UP -> getCellOrNull(i - 1, j)
        DOWN -> getCellOrNull(i + 1, j)
        LEFT -> getCellOrNull(i, j - 1)
        RIGHT -> getCellOrNull(i, j + 1)
    }
}
fun <T> createGameBoard(width: Int): GameBoard<T> = object : GameBoard<T>, SquareBoard by createSquareBoard(width) {
    private val cellValues: MutableMap<Cell, T?> = getAllCells().associateWith { null }.toMutableMap()

    override fun get(cell: Cell): T? = cellValues[cell]

    override fun set(cell: Cell, value: T?) {
        cellValues[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> =
        cellValues.filterValues(predicate).keys

    override fun find(predicate: (T?) -> Boolean): Cell? =
        cellValues.entries.find { (_, value) -> predicate(value) }?.key

    override fun any(predicate: (T?) -> Boolean): Boolean = cellValues.values.any(predicate)

    override fun all(predicate: (T?) -> Boolean): Boolean = cellValues.values.all(predicate)
}


