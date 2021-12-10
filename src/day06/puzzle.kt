package day06

import readInput

fun main() {

    fun part1(input: List<String>): Int {
        val newFishTimer = 8
        val oldFishTimer = 6
        val parsed = input.first().trim().split(",").map(String::toInt)

        fun reproduce(curDay: Int, finalDay: Int, acc: List<Int>): List<Int> {
            println("$curDay, ${acc.count()}")
            if (curDay == finalDay) {
                return acc
            }
            val birthCount = acc.count { it == 0 }
            val newFish = IntArray(birthCount) { newFishTimer }.toList()
            return reproduce(curDay.inc(), finalDay, acc.map {
                when (it) {
                    0 -> oldFishTimer
                    else -> it.dec()
                }
            } + newFish)
        }
        return reproduce(0, 80, parsed).count()
    }


    fun part2(input: List<String>): Long {
        val newFishTimer = 8
        val oldFishTimer = 6
        val parsed = input.first().trim().split(",").map(String::toInt)
            .groupingBy { it }.eachCount()
            .toList()
            .map { Pair(it.first, it.second.toLong()) }

        fun reproduce(curDay: Int, finalDay: Int, acc: List<Pair<Int, Long>>): List<Pair<Int, Long>> {
            println("$curDay, ${acc.count()}")
            if (curDay == finalDay) {
                return acc
            }
            val birthCount = acc.filter { it.first == 0 }.sumOf { it.second }
            val next = acc.map {
                when (it.first) {
                    0 -> Pair(oldFishTimer, it.second)
                    else -> Pair(it.first.dec(), it.second)
                }
            }.groupBy {
                it.first
            }.mapValues {
                it.value.reduce { acc, cur ->
                    Pair(acc.first, acc.second + cur.second)
                }
            }.values.plus(Pair(newFishTimer, birthCount))

            return reproduce(curDay.inc(), finalDay, next.toList())
        }
        return reproduce(0, 256, parsed).sumOf { it.second }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day06", test = true)
    val part1Expected = 5934
    val part1Actual = part1(testInput)
    check(part1Actual == part1Expected) {
        "part1: expected $part1Expected, got $part1Actual"
    }

    val input = readInput("day06")
    println(part1(input))
    println(part2(input))
}
