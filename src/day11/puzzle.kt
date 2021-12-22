package day11

import readInput

const val DAY = "day11"

data class GridLocation(
    val row: Int,
    val col: Int,
)

data class MutableOctopus(
    var energy: Int,
    var flashed: Boolean = false
) {
    fun inc() {
        if (flashed) return
        else {
            if (energy == 9) {
                flashed = true
            } else {
                energy += 1
            }
        }
    }

    fun canProliferate() = flashed && energy == 9
}

data class OctopusGrid(
    val grid: List<List<MutableOctopus>>
) {
    private val maxY = grid.count() - 1
    private val maxX = grid.first().count() - 1 // this assumes all rows have the same count

    val adjacencyList = grid.mapIndexed { row, rowItems ->
        rowItems.mapIndexed { col, _ ->
            val n = if (row != 0) GridLocation(row - 1, col) else null
            val ne = if (row != 0 && col != maxX) GridLocation(row - 1, col + 1) else null
            val e = if (col != maxX) GridLocation(row, col + 1) else null
            val se = if (row != maxY && col != maxX) GridLocation(row + 1, col + 1) else null
            val s = if (row != maxY) GridLocation(row + 1, col) else null
            val sw = if (row != maxY && col != 0) GridLocation(row + 1, col - 1) else null
            val w = if (col != 0) GridLocation(row, col - 1) else null
            val nw = if (row != 0 && col != 0) GridLocation(row - 1, col -1) else null
            Pair(
                GridLocation(row, col),
                listOfNotNull(n, ne, e, se, s, sw, w, nw)
            )
        }
    }.flatten().toMap()

    fun getOctopus(loc: GridLocation): MutableOctopus = grid[loc.row][loc.col]

    companion object {
        fun parse(input: List<String>): OctopusGrid {
            return OctopusGrid(input.map { row ->
                row.map { MutableOctopus(it.digitToInt()) }
            })
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val grid = OctopusGrid.parse(input)
        var flashCount = 0

        fun performStep(grid: OctopusGrid) {
            grid.adjacencyList.keys.forEach {
                grid.getOctopus(it).inc()
            }
            while (grid.adjacencyList.keys.any { grid.getOctopus(it).canProliferate() }) {
                grid.adjacencyList.filter { grid.getOctopus(it.key).canProliferate() }.forEach { item ->
                    item.value.forEach { adj ->
                        grid.getOctopus(adj).inc()
                    }
                    grid.getOctopus(item.key).energy = 0
                    flashCount += 1
                }
            }
            grid.adjacencyList.keys.map { grid.getOctopus(it) }.forEach {
                it.flashed = false
            }
        }

        repeat(100) {
            performStep(grid)
        }

        return flashCount
    }


    fun part2(input: List<String>): Int {
        val grid = OctopusGrid.parse(input)
        var iterationCount = 0

        fun performStep(grid: OctopusGrid) {
            grid.adjacencyList.keys.forEach {
                grid.getOctopus(it).inc()
            }
            while (grid.adjacencyList.keys.any { grid.getOctopus(it).canProliferate() }) {
                grid.adjacencyList.filter { grid.getOctopus(it.key).canProliferate() }.forEach { item ->
                    item.value.forEach { adj ->
                        grid.getOctopus(adj).inc()
                    }
                    grid.getOctopus(item.key).energy = 0
                }
            }

        }

        val allOctopuses = grid.adjacencyList.keys.map { grid.getOctopus(it) }
        while(allOctopuses.any { !(it.energy == 0 && it.flashed)}) {
            allOctopuses.forEach { it.flashed = false }
            performStep(grid)
            iterationCount += 1
        }

        return iterationCount
    }

    fun test(expected: Int, part: (List<String>) -> Int) {
        val testInput = readInput(DAY, test = true)
        val actual = part.invoke(testInput)
        check(actual == expected) {
            "$part expected $expected, got $actual"
        }
    }

    test(1656, ::part1)
    test(195, ::part2)

    val input = readInput(DAY)
    println(part1(input))
    println(part2(input))
}
