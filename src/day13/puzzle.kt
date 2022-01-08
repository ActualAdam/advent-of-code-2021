package day13

import readInput

const val DAY = "day13"

fun main() {
    data class Dot(
        val x: Int,
        val y: Int,
    )

    data class FoldInstruction(
        val axis: Char,
        val value: Int,
    )

    fun foldY(dots: Set<Dot>, fold: Int): Set<Dot> = dots.map { dot ->
        if (dot.y > fold) {
            val distanceFromFold = dot.y - fold
            Dot(dot.x, fold - distanceFromFold)
        } else dot
    }.toSet()

    fun foldX(dots: Set<Dot>, fold: Int): Set<Dot> = dots.map { dot ->
        if (dot.x > fold) {
            val distanceFromFold = dot.x - fold
            Dot(fold - distanceFromFold, dot.y)
        } else dot
    }.toSet()

    fun parseDots(input: List<String>) = input.filter { it.contains(',') }.map { line ->
        line.split(',').map { it.toInt() }.let {
            Dot(it[0], it[1])
        }
    }.toSet()

    fun parseInstructions(input: List<String>): List<FoldInstruction> {
        return input.filter { it.startsWith("fold") }
            .map {
                it.split(' ').last()
                .split('=').let {
                    FoldInstruction(it[0].single(), it[1].toInt())
                }
            }
    }

    fun part1(input: List<String>): Int {

        val dots = parseDots(input)

        val firstInstruction = parseInstructions(input).first()

        return if (firstInstruction.axis == 'y') {
            foldY(dots, firstInstruction.value)
        } else {
            foldX(dots, firstInstruction.value)
        }.size
    }

    fun part2(input: List<String>): Set<Dot> {
        val dots = parseDots(input)

        val instructions = parseInstructions(input)

        val folded = instructions.fold(dots) { acc, cur ->
            if (cur.axis == 'y') {
                foldY(acc, cur.value)
            } else {
                foldX(acc, cur.value)
            }
        }

        val cols = folded.maxOf { it.x } + 1
        val rows = folded.maxOf { it.y } + 1
        println("$cols, $rows")
        val grid = Array(rows) { CharArray(cols) { '.' } }
        folded.forEach {
            grid[it.y][it.x] = '#'
        }
        grid.forEach {
            println(it.concatToString())
        }
        return folded
    }

    fun test(expected: Int, part: (List<String>) -> Int) {
        val testInput = readInput(DAY, test = true)
        val actual = part.invoke(testInput)
        check(actual == expected) {
            "$part expected $expected, got $actual"
        }
    }

    test(17, ::part1)
//    test(-1, ::part2)

    val input = readInput(DAY)
    println(part1(input))
    println(part2(input))
}
