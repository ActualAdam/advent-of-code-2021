package day09

import readInput

const val DAY = "day09"

fun main() {

    fun part1(input: List<String>): Int {
        val grid = input.map { row ->
            row.map { it.digitToInt() }
        }
        val maxY = grid.count() - 1
        val maxX = grid.first().count() - 1 // this assumes all rows have the same count

        val adjacencyList = grid.mapIndexed { y, row ->
            // remember that the double array refers to cartesian positions in opposite order because of the data structure: y,x row,col
            row.mapIndexed { x, height ->
                val left = if (x != 0) {
                    grid[y][x - 1]
                } else {
                    null
                }
                val right = if (x != maxX) {
                    grid[y][x + 1]
                } else {
                    null
                }
                val up = if (y != 0) {
                    grid[y - 1][x]
                } else {
                    null
                }
                val down = if (y != maxY) {
                    grid[y + 1][x]
                } else {
                    null
                }
                Pair(height, listOfNotNull(left, right, up, down))
            }
        }.flatten()

        val lowPoints = adjacencyList.filter {
            it.second.all { adj -> adj > it.first }
        }.map { it.first }


        return lowPoints.sumOf { it.inc() }
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { row ->
            row.map { it.digitToInt() }
        }

        data class Location(
            val row: Int,
            val col: Int,
        )

        val maxY = grid.count() - 1
        val maxX = grid.first().count() - 1 // this assumes all rows have the same count

        val adjacencyList = grid.mapIndexed { row, rowItems ->
            rowItems.mapIndexed { col, _ ->
                val left = if (col != 0) Location(row, col - 1) else null
                val right = if (col != maxX) Location(row, col + 1) else null
                val up = if (row != 0) Location(row - 1, col) else null
                val down = if (row != maxY) Location(row + 1, col) else null
                Pair(
                    Location(row, col),
                    listOfNotNull(left, right, up, down)
                )
            }
        }.flatten().toMap()

        fun getHeight(loc: Location): Int = grid[loc.row][loc.col]

        val lowPoints = adjacencyList.filter {
            it.value.all { adj -> getHeight(adj) > getHeight(it.key) }
        }.map { it.key }

        fun getBasinSize(lowPoint: Location): Int {
            val visited = mutableListOf<Location>()

            val next = ArrayDeque<Location>()
            next.addFirst(lowPoint)

            while (next.isNotEmpty()) {
                val cur = next.removeFirst()
                if (getHeight(cur) == 9 || visited.contains(cur)) continue
                visited.add(cur)
                next.addAll(adjacencyList[cur]!!)
            }

            return visited.size
        }

        val basinSizes = lowPoints.map { getBasinSize(it) }
        return basinSizes.sorted().takeLast(3).reduce(Int::times)
    }


    fun test(expected: Int, part: (List<String>) -> Int) {
        val testInput = readInput(DAY, test = true)
        val actual = part.invoke(testInput)
        check(actual == expected) {
            "$part expected $expected, got $actual"
        }
    }

    test(15, ::part1)
    test(1134, ::part2)

    val input = readInput(DAY)
    println(part1(input))
    println(part2(input))
}
