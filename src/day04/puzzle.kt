package day04

import readInput

data class BingoSquare(
    val row: Int,
    val column: Int,
    val value: Int,
    var marked: Boolean = false,
)

data class BingoCard(
    val squares: List<BingoSquare>,
    var winningCall: Int? = null,
) {
    private val squaresByRow = squares.groupBy { sq -> sq.row }
    private val squaresByColumn = squares.groupBy { sq -> sq.column }

    fun isWinner(): Boolean {
        squaresByRow.forEach {
            if (it.value.all { sq -> sq.marked }) {
                return true
            }
        }
        squaresByColumn.forEach {
            if (it.value.all { sq -> sq.marked }) {
                return true
            }
        }
        return false
    }

    fun score(): Int {
        val sumUnmarked = squares.filter { x -> !x.marked }.fold(0) { acc, sq ->
            acc + sq.value
        }

        return sumUnmarked * winningCall!!
    }

    companion object {
        fun parse(input: List<String>): BingoCard {
            require(input.size == 5)
            val squares = input.mapIndexed { rowIdx, rowVal ->
                rowVal.trim().split("\\s+".toRegex()).mapIndexed { colIdx, sqVal ->
                    BingoSquare(
                        row = rowIdx,
                        column = colIdx,
                        value = sqVal.toInt(),
                        marked = false,
                    )
                }
            }.flatten()
            return BingoCard(squares)
        }
    }
}

fun main() {


    fun part1(input: List<String>): Int {
        val calls = input.first().split(",").map { it.toInt() }
        val cardStrings = input.drop(2).filterNot { it.isBlank() }.chunked(5)
        val cards = cardStrings.map { BingoCard.parse(it) }

        val allSquaresByValue = cards.flatMap { card -> card.squares }.groupBy { sq -> sq.value }

        calls.forEach { call ->
            allSquaresByValue[call]?.forEach { sq -> sq.marked = true }
            cards.singleOrNull(BingoCard::isWinner)?.let {
                return it.copy(winningCall = call).score()
            }
        }

        throw RuntimeException("no winning cards")
    }


    fun part2(input: List<String>): Int {
        val calls = input.first().split(",").map { it.toInt() }
        val cardStrings = input.drop(2).filterNot { it.isBlank() }.chunked(5)
        val cards = cardStrings.map { BingoCard.parse(it) }.toMutableList()

        var winners: MutableList<Int> = mutableListOf()

        calls.forEach { call ->
            cards.forEach { card ->
                card.squares.singleOrNull{ it.value == call }?.let {
                    it.marked = true
                    if (card.isWinner() && card.winningCall == null) {
                        card.winningCall = call
                        winners.add(card.score())
                    }
                }
            }
        }

        return winners.last()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day04", test = true)
    val part1Expected = 4512
    val part1Actual = part1(testInput)
    check(part1Actual == part1Expected) {
        "part1: expected $part1Expected, got $part1Actual"
    }
    val part2Expected = 1924
    val part2Actual = part2(testInput)
    check(part2Actual == part2Expected) {
        "part2: expected $part2Expected, got $part2Actual"
    }

    val input = readInput("day04")
    println(part1(input))
    println(part2(input))
}
