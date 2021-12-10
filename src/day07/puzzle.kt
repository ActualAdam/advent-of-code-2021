package day07

import readInput
import kotlin.math.absoluteValue

const val DAY = "day07"

fun main() {

    fun part1(input: List<String>): Int {
        val initialPositions = input.first().trim().split(",").map { it.toInt() }
        val max = initialPositions.maxOrNull()!!
        val fuelCosts = (0..max).associateWith { target ->
            initialPositions.fold(0) { acc, cur ->
                acc + (cur - target).absoluteValue
            }
        }
        return fuelCosts.values.minOrNull()!!
    }

    fun triangleNumber(n: Int): Int = n * (n + 1) / 2

    fun part2(input: List<String>): Int {
        val initialPositions = input.first().trim().split(",").map { it.toInt() }
        val max = initialPositions.maxOrNull()!!
        val fuelCosts = (0..max).associateWith { target ->
            initialPositions.fold(0) { acc, cur ->
                val distance = (cur - target).absoluteValue
                acc + triangleNumber(distance)
            }
        }
        return fuelCosts.values.minOrNull()!!
    }

    fun test(expected: Int, part: (List<String>) -> Int) {
        val testInput = readInput(DAY, test = true)
        val actual = part.invoke(testInput)
        check(actual == expected)   {
            "$part expected $expected, gpt $actual"
        }
    }

    test(37, ::part1)
    test(168, ::part2)

    val input = readInput(DAY)
    println(part1(input))
    println(part2(input))
}
