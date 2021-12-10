package day05

import readInput
import kotlin.math.absoluteValue

data class Point(
    val x: Int,
    val y: Int,
)

data class Segment(
    val end1: Point,
    val end2: Point,
) {
    fun isHorizontal() = end1.x == end2.x

    fun isVertical() = end1.y == end2.y

    fun isDiagonal() = (end1.y - end2.y).absoluteValue == (end1.x - end2.x).absoluteValue
}

fun enumeratePoints(segment: Segment): List<Point> {
    tailrec fun nextPoint(cur: Point, acc: List<Point>): List<Point> {
        if (cur == segment.end2) {
            return acc
        }

        val nextX = when {
            segment.end2.x > cur.x -> cur.x.inc()
            segment.end2.x < cur.x -> cur.x.dec()
            else -> cur.x
        }
        val nextY = when {
            segment.end2.y > cur.y -> cur.y.inc()
            segment.end2.y < cur.y -> cur.y.dec()
            else -> cur.y
        }

        val nextPoint = Point(nextX, nextY)
        return nextPoint(nextPoint, acc + nextPoint)
    }

    return nextPoint(segment.end1, listOf(segment.end1))
}

fun parseSegments(input: List<String>) = input.map { inputLine ->
    val (end1, end2) = inputLine.split("->").map {
        val (x, y) = it.trim().split(",").map(String::toInt)
        Point(x, y)
    }
    Segment(end1, end2)
}

fun overlappingSegmentCountByPoint(interestingSegments: List<Segment>): Map<Point, Int> {
    val counts = interestingSegments.map {
        enumeratePoints(it)
    }
        .flatten()
        .groupingBy { it }
        .eachCount()
    return counts
}

fun main() {
    fun part1(input: List<String>): Int {
        val segments = parseSegments(input)
        val interestingSegments = segments.filter {
            it.isHorizontal() || it.isVertical()
        }

        val counts = overlappingSegmentCountByPoint(interestingSegments)

        return counts.values.count { it > 1 }
    }


    fun part2(input: List<String>): Int {
        val segments = parseSegments(input)
        val interestingSegments = segments.filter {
            it.isHorizontal()
                    || it.isVertical()
                    || it.isDiagonal()
        }

        val counts = overlappingSegmentCountByPoint(interestingSegments)

        return counts.values.count { it > 1 }

    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("day05", test = true)
    val part1Expected = 5
    val part1Actual = part1(testInput)
    check(part1Actual == part1Expected) {
        "part1: expected $part1Expected, got $part1Actual"
    }
    val part2Expected = 12
    val part2Actual = part2(testInput)
    check(part2Actual == part2Expected) {
        "part2: expected $part2Expected, got $part2Actual"
    }

    val input = readInput("day05")
    println(part1(input))
    println(part2(input))
}
