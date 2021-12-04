package day03

import readInput

/**
 * translate the integer to binary and flip each bit.
 * @return the one's complement as an integer
 */
fun Int.binaryOnesComplement() = ((1 shl this.bitLength()) - 1) xor this

/**
 * @return how many binary digits the given integer has
 */
fun Int.bitLength() = Integer.toBinaryString(this).length

/**
 * assumes each binary string is the same length, leading zeros
 *
 * counts the number of bits set to 1 for each position in the list of numbers
 * @return a list of integers representing the counts for each position left to right
 */
fun computeBitSums(binaryStrings: List<String>): List<Int> {
    return binaryStrings.map { row ->
        row.map { it.digitToInt() }
    }.reduce { acc, row ->
        acc.zip(row, Int::plus)
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        fun computeGamma(input: List<String>): Int {
            val rowCount = input.size

            val gamma = computeBitSums(input).joinToString("") {
                if (it < rowCount / 2) {
                    0
                } else {
                    1
                }.toString()
            }.toInt(2)
            return gamma
        }

        val gamma = computeGamma(input)
        val epsilon = gamma.binaryOnesComplement()

        return gamma * epsilon
    }

    fun part2(input: List<String>): Int {
        tailrec fun sieve(
            results: List<String>,
            bitCriteriaFunction: (List<String>) -> String,
            stringIndex: Int
        ): List<String> {
            if (results.size == 1) {
                return results
            }
            val bitCriteria = bitCriteriaFunction.invoke(results)
            val nextResults = results.filter { it[stringIndex] == bitCriteria[stringIndex] }
            return sieve(nextResults, bitCriteriaFunction, stringIndex.inc())
        }

        val o2BitCriteriaFunction: (List<String>) -> String = { binaryStrings ->
            val rowCount = binaryStrings.size
            val bitSums = computeBitSums(binaryStrings)
            bitSums
                .joinToString("") { //mapping function
                    if (it >= rowCount - it) {
                        1
                    } else {
                        0
                    }.toString()
                }
        }

        val co2BitCriteriaFunction: (List<String>) -> String = { binaryStrings ->
            val rowCount = binaryStrings.size
            val bitSums = computeBitSums(binaryStrings)
            bitSums
                .joinToString("") {
                    if (it < rowCount - it) {
                        1
                    } else {
                        0
                    }.toString()
                }
        }

        val o2Rating = sieve(input, o2BitCriteriaFunction, 0).single().toInt(2)
        val co2Rating = sieve(input, co2BitCriteriaFunction, 0).single().toInt(2)

        return o2Rating * co2Rating
    }


// test if implementation meets criteria from the description, like:
    val testInput = readInput("day03", test = true)
    check(part1(testInput) == 198)
    val part2Test = part2(testInput)
    check(part2Test == 230) {
        "expected 230 but got $part2Test"
    }

    val input = readInput("day03")
    println(part1(input))
    println(part2(input))
}
