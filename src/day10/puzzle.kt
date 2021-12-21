package day10

import readInput

const val DAY = "day10"

fun main() {

    val scoring = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137,
        '(' to 1,
        '[' to 2,
        '{' to 3,
        '<' to 4,
    )

    val openChars = setOf(
        '(',
        '[',
        '{',
        '<',
    )

    fun isClosing(char: Char) = !openChars.contains(char)

    val matching = mapOf(
        ')' to '(',
        '(' to ')',
        ']' to '[',
        '[' to ']',
        '{' to '}',
        '}' to '{',
        '<' to '>',
        '>' to '<',
    )

    fun part1(input: List<String>): Long {
        fun parseLine(line: String): Int? {
            val parsedOpenings = ArrayDeque<Char>()
            for (char in line) {
                if (isClosing(char)) {
                    if (parsedOpenings.isEmpty()) return scoring[char]
                    if (matching[char] != parsedOpenings.removeLast()) return scoring[char]
                } else {
                    parsedOpenings.addLast(char)
                }
            }
            return null
        }

        return input.mapNotNull { parseLine(it) }.sum().toLong()
    }


    fun part2(input: List<String>): Long {

        fun isCorrupt(line: String): Boolean {
            val parsedOpenings = ArrayDeque<Char>()
            for (char in line) {
                if (isClosing(char)) {
                    if (parsedOpenings.isEmpty()) return true
                    if (matching[char] != parsedOpenings.removeLast()) return true
                    continue
                }
                parsedOpenings.addLast(char)
            }
            return false
        }

        input.filter {
            isCorrupt(it)
        }

        fun scoreIncompleteLine(line: String): Long {
            val parsed = ArrayDeque<Char>()
            for (char in line) {
                if (openChars.contains(char)) {
                    parsed.addLast(char)
                } else {
                    check(matching[char] == parsed.last()) { "expected a closing ${parsed.last()}, but got a $char" }
                    parsed.removeLast()
                }
            }
            val reversed = parsed.reversed()
            val scored = reversed
                .map {
                    scoring[it]!!
                }
            return scored.fold(0L) { acc, x ->
                acc * 5 + x
            }
        }

        val scores = input
            .filterNot { isCorrupt(it) }
            .map { scoreIncompleteLine(it) }

        val sorted = scores.sorted()
        return sorted[scores.size / 2]
    }

    fun test(expected: Long, part: (List<String>) -> Long) {
        val testInput = readInput(DAY, test = true)
        val actual = part.invoke(testInput)
        check(actual == expected) {
            "$part expected $expected, got $actual"
        }
    }

    test(26397, ::part1)
    test(288957, ::part2)

    val input = readInput(DAY)
    println(part1(input))
    println(part2(input))
    // guess 237311081 and it was too low
}
