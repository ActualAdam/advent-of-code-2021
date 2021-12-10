package day00

import readInput

const val DAY = "day00"

fun main() {

    fun part1(input: List<String>): Int {
        return -1
    }


    fun part2(input: List<String>): Int {
        return -1
    }

    fun test(expected: Int, part: (List<String>) -> Int) {
        val testInput = readInput(DAY, test = true)
        val actual = part.invoke(testInput)
        check(actual == expected)   {
            "$part expected $expected, gpt $actual"
        }
    }

    test(0, ::part1)
    test(-1, ::part2)

    val input = readInput(DAY)
    println(part1(input))
    println(part2(input))
}
