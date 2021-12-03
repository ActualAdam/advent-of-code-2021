package day00

import readInput

fun main() {

    fun part1(input: List<String>): Int {
        return -1
    }


    fun part2(input: List<String>): Int {
        return -1
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day00", test = true)
    check(part1(testInput) == 0)

    val input = readInput("day00")
    println(part1(input))
    println(part2(input))
}
