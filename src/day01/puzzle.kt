package day01

import readInput

fun main() {
    fun countIncreases(measurements: List<Int>) = measurements.filterIndexed { i, v ->
        i != 0 && v > measurements[i - 1]
    }.count()

    fun part1(input: List<String>): Int {
        val measurements = input.map { it.toInt() }
        return countIncreases(measurements)
    }

    fun computeSlidingWindows(rawMeasurements: List<Int>) =
        rawMeasurements.mapIndexed { i, v ->
            if (rawMeasurements.size - i < 3) {
                Int.MIN_VALUE
            } else {
                listOf(v, rawMeasurements[i + 1], rawMeasurements[i + 2]).sum()
            }
        }

    fun part2(input: List<String>): Int {
        val rawMeasurements = input.map { it.toInt() }
        val slidingWindows = computeSlidingWindows(rawMeasurements)
        return countIncreases(slidingWindows)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day01", test = true)
    check(part1(testInput) == 7)

    val input = readInput("day01")
    println(part1(input))
    println(part2(input))
}
