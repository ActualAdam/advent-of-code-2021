package day14

import readInput

const val DAY = "day14"

fun main() {
    data class PairInsertionRule(
        val pair: String,
        val insertion: Char,
    )

    fun parseTemplate(input: List<String>) = input.first()

    fun parseRules(input: List<String>) = input.drop(2).map { line ->
        line.split(" -> ").let {
            PairInsertionRule(it[0], it[1].single())
        }
    }

    fun part1(input: List<String>): Long {
        val template = parseTemplate(input)
        val rules = parseRules(input)

        fun polymerize(template: String, steps: Int, rules: List<PairInsertionRule>): String {
            tailrec fun run(acc: String, step: Int): String {
                if (step > steps) return acc
                val windows = acc.windowed(2)
                val next = windows.map { pair ->
                    rules.singleOrNull() { it.pair == pair }?.let {
                        listOf(pair[0], it.insertion).joinToString("")
                    } ?: pair
                }.joinToString("") + template.last()
                return run(next, step.inc())
            }
            return run(template, 1)
        }

        val fullyPolymerized = polymerize(template, 10, rules)

        val counts = fullyPolymerized.toList().groupingBy { it }.eachCount()
        val min = counts.values.minOf { it }
        val max = counts.values.maxOf { it }
        return (max - min).toLong()
    }

    fun part2(input: List<String>): Long {
        val template = parseTemplate(input) + " "
        val rules = parseRules(input)

        // imagine a sliding window around each pair of characters in the chain.
        // but I only care about the number of occurrences of each pair, so I just count the occurrences to start
        val initialCounts = template.windowed(2).groupingBy { it }.eachCount().mapValues { it.value.toLong() }

        tailrec fun polymerize(acc: Map<String, Long>, times: Int = 40): Map<String, Long> {
            if (times == 0) return acc
            val mapped = acc.flatMap { entry ->
                rules.singleOrNull { it.pair == entry.key }?.let { rule ->
                    listOf(
                        listOf(entry.key[0], rule.insertion).joinToString("") to entry.value,
                        listOf(rule.insertion, entry.key[1]).joinToString("") to entry.value,
                    )
                } ?: listOf(entry.toPair())
            }
            // each original pair yielded 2 new pairs, some of which are repeated throughout the list. I need to sum
            // their counts into a single map
            val reduced = mapped
                .groupBy({ it.first }, { it.second })
                .mapValues { entry ->
                    val sum = entry.value.sum()
                    if (sum > 0) sum else 0
                }
            return polymerize(reduced, times.dec())
        }

        // group counts by first letter of the pair
        val counts = polymerize(initialCounts).asSequence()
            .groupBy({ it.key[0] }, { it.value })
            .mapValues { it.value.sum() }

        val max = counts.maxOf { it.value }
        val min = counts.minOf { it.value }
        return max - min
    }

    fun test(expected: Long, part: (List<String>) -> Long) {
        val testInput = readInput(DAY, test = true)
        val actual = part.invoke(testInput)
        check(actual == expected) {
            "$part expected $expected, got $actual"
        }
    }

    test(1588, ::part1)
    test(2188189693529L, ::part2)

    val input = readInput(DAY)
    println(part1(input))
    println(part2(input))
}
