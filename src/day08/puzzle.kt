package day08

import readInput

const val DAY = "day08"

/**
0:      1:      2:      3:      4:
aaaa    ....    aaaa    aaaa    ....
b    c  .    c  .    c  .    c  b    c
b    c  .    c  .    c  .    c  b    c
....    ....    dddd    dddd    dddd
e    f  .    f  e    .  .    f  .    f
e    f  .    f  e    .  .    f  .    f
gggg    ....    gggg    gggg    ....

5:      6:      7:      8:      9:
aaaa    aaaa    aaaa    aaaa    aaaa
b    .  b    .  .    c  b    c  b    c
b    .  b    .  .    c  b    c  b    c
dddd    dddd    ....    dddd    dddd
.    f  e    f  .    f  e    f  .    f
.    f  e    f  .    f  e    f  .    f
gggg    gggg    ....    gggg    gggg


1: 2   c  f
4: 4  bcd f
7: 3 a c  f
8: 7 abcdefg

0: 6 abc efg ?
6: 6 ab defg ?
9: 6 abcd fg

2: 5 a cde g
3: 5 a cd fg 3-2=f 3-5=c; 2-3=e 2-5=ce; 5-2=bf 5-3=b ;  when 3 is on the left, difference count is 1
5: 5 ab d fg
 */

/**
 * 1 has 2 chars
 * 4 has 4 chars
 * 7 has 3 chars
 * 8 has 7 chars
 * a: 7 - 1
 * 3: put each 5-wire digit on the left and try subtracting each of the other 2 5-wire digits. the one where the difference set is size 1 for both subtractions
 * b: 4 - 3
 * g: 3 - 4 - a
 * e: 8 - (3 + 4)
 * 9 has 6 characters and doesn't have an e
 * 2 (a cde g) has 2 chars after subracting g, e, a. (so does 7 and 1, but they are already known
 * c: is the one in 1 that isn't in 9-2
 * f: 9-2 -b
 * d: is the unknown letter in 4
 * 3 doesn't have a b
 * 5 doesn't have a c
 *
 */
fun main() {

    fun part1(input: List<String>): Int {
        // get just the output values
        val outputValues = input
            .map { inputLine ->
                inputLine.split("|").let {
                    it[1].trim().split(" ")
                }
            }
        val identifyingCounts = listOf(2, 4, 3, 7)
        return outputValues.flatten().count {
            identifyingCounts.contains(it.count())
        }
    }


    fun part2(input: List<String>): Int {
        fun decode(output: List<String>, key: Map<Set<Char>, Int>): Int {
            require(output.size == 4)
            return output.map { key[it.toSet()]!! }.joinToString("").toInt()
        }

        val displays = input.map { inputLine ->
            inputLine.split("|").let {
                Pair(it[0].trim().split(" "), it[1].trim().split(" "))
            }
        }
        val decoded = displays.map { display ->
            val allDigits = display.first
            val output = display.second
            val one = allDigits.single { it.count() == 2 }
                .toSet()          // these have unique wire counts, so they are easily identified by count
            val four = allDigits.single { it.count() == 4 }.toSet()         // "
            val seven = allDigits.single { it.count() == 3 }.toSet()        // "
            val eight = allDigits.single { it.count() == 7 }.toSet()        // "
            val signalWiresBySegment = mutableMapOf<Char, Char>()
            val a = seven - one
            signalWiresBySegment['a'] = a.single()
            val fiveWireDigits = allDigits.filter { it.count() == 5 }.map { it.toSet() }
            val three = fiveWireDigits.single { outer ->
                val others = fiveWireDigits.filter { inner ->
                    inner != outer
                }
                val diff0 = outer - others[0]
                val diff1 = outer - others[1]
                diff0.count() == diff1.count()
            }
            val b = four - three
            signalWiresBySegment['b'] = b.single()
            val g = three - four - a
            signalWiresBySegment['g'] = g.single()
            val e = eight - (three + four)
            signalWiresBySegment['e'] = e.single()
            val nine = allDigits.single { it.count() == 6 && !it.contains(e.single()) }.toSet()

            ///  line

            val two = allDigits.map { it.toSet() }
                .filterNot { it == seven || it == one }
                .single { (it - a - e - g).count() == 2 }

            val c = one - (nine - two)
            signalWiresBySegment['c'] = c.single()

            val allWires = "abcdefg".toSet()

            val five = allDigits.single {
                it.toSet() == allWires - c - e
            }
            val six = allDigits.single {
                it.toSet() == allWires - c
            }

            val f = nine - two - b
            signalWiresBySegment['f'] = f.single()

            val d = four - signalWiresBySegment.values.toSet()
            signalWiresBySegment['d'] = d.single()

            val zero = allDigits.single {
                it.toSet() == allWires - d
            }


            val displayDigitKey = mapOf<Set<Char>, Int>(
                one to 1,
                two to 2,
                three to 3,
                four to 4,
                five.toSet() to 5,
                six.toSet() to 6,
                seven to 7,
                eight to 8,
                nine to 9,
                zero.toSet() to 0
            )

            decode(output, displayDigitKey)
        }
        return decoded.sum()
    }

    fun test(expected: Int, part: (List<String>) -> Int) {
        val testInput = readInput(DAY, test = true)
        val actual = part.invoke(testInput)
        check(actual == expected) {
            "$part expected $expected, gpt $actual"
        }
    }

    test(26, ::part1)
    test(61229, ::part2)

    val input = readInput(DAY)
    println(part1(input))
    println(part2(input))
}
