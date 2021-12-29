package day12

import readInput

const val DAY = "day12"

fun main() {

    fun part1(input: List<String>): Int {
        val edgeList = input.map { line ->
            line.split("-").let { Pair(it[0], it[1]) }
        }
        val adjacencyList = mutableMapOf<String, List<String>>()
        edgeList.forEach { edge ->
            adjacencyList.merge(edge.first, listOf(edge.second)) { old, new -> old + new }
            adjacencyList.merge(edge.second, listOf(edge.first)) { old, new -> old + new }
        }

        fun canRevisit(str: String) = str.all { it.isUpperCase() }


        fun countPaths(): Int {
            val paths = mutableListOf<List<String>>()
            val next = mutableListOf(listOf("start"))

            while (next.isNotEmpty()) {
                val path = next.removeFirst()
                val cur = path.last()

                if (cur == "end") {
                    paths.add(path)
                } else {
                    adjacencyList[cur]!!.forEach { adj ->
                        if (!path.contains(adj) || canRevisit(adj)) {
                            next += path + adj
                        }
                    }
                }
            }
            return paths.count()
        }
        return countPaths()
    }


    fun part2(input: List<String>): Int {
        val edgeList = input.map { line ->
            line.split("-").let { Pair(it[0], it[1]) }
        }
        val adjacencyList = mutableMapOf<String, List<String>>()
        edgeList.forEach { edge ->
            adjacencyList.merge(edge.first, listOf(edge.second)) { old, new -> old + new }
            adjacencyList.merge(edge.second, listOf(edge.first)) { old, new -> old + new }
        }

        fun isBig(str: String) = str.all { it.isUpperCase() }

        fun countPaths(): Int {
            val paths = mutableListOf<List<String>>()
            val next = mutableListOf(listOf("start"))

            while (next.isNotEmpty()) {
                val path = next.removeFirst()
                val cur = path.last()

                if (cur == "end") {
                    paths.add(path)
                } else {
                    adjacencyList[cur]!!.forEach { adj ->
                        if (path.groupingBy { it }.eachCount().any { !isBig(it.key) && it.value == 2 }) {
                            if ((adj != "start" && !path.contains(adj)) || isBig(adj)) {
                                next += path + adj
                            }
                        } else {
                            if (adj != "start") {
                                next += path + adj
                            }
                        }
                    }
                }
            }
            paths.forEach {
                println(it)
            }
            return paths.count()
        }
        return countPaths()
    }

    fun test(expected: Int, part: (List<String>) -> Int) {
        val testInput = readInput(DAY, test = true)
        val actual = part.invoke(testInput)
        check(actual == expected) {
            "$part expected $expected, got $actual"
        }
    }

    test(10, ::part1)
    test(36, ::part2)

    val input = readInput(DAY)
    println(part1(input))
    println(part2(input))
}
