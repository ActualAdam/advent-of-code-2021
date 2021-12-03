package day02

import readInput

enum class Direction {
    up,
    down,
    forward,
}

interface Position<T> {
    fun applyCommand(command: Command): T
}

data class PositionPart1(
    val horiz: Int = 0,
    val depth: Int = 0,
) : Position<PositionPart1> {
    override fun applyCommand(command: Command): PositionPart1 {
        return when (command.direction) {
            Direction.forward -> this.copy(horiz = this.horiz + command.amount)
            Direction.down -> this.copy(depth = this.depth + command.amount)
            Direction.up -> this.copy(depth = this.depth - command.amount)
        }
    }
}

data class PositionPart2(
    val horiz: Int = 0,
    val depth: Int = 0,
    val aim: Int = 0
) : Position<PositionPart2> {
    override fun applyCommand(command: Command): PositionPart2 {
        return when (command.direction) {
            Direction.down -> this.copy(aim = aim + command.amount)
            Direction.up -> this.copy(aim = this.aim - command.amount)
            Direction.forward -> this.copy(
                horiz = this.horiz + command.amount,
                depth = this.depth + this.aim * command.amount
            )
        }
    }
}

data class Command(
    val direction: Direction,
    val amount: Int,
) {
    companion object {
        fun parseInput(input: String): Command {
            val (dir, dist) = input.split(" ")
            return Command(Direction.valueOf(dir), dist.toInt())
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val finalPosition = input.fold(PositionPart1()) { acc, x ->
            acc.applyCommand(Command.parseInput(x))
        }
        return finalPosition.horiz * finalPosition.depth
    }

    fun part2(input: List<String>): Int {
        val finalPosition = input.fold(PositionPart2()) { acc, x ->
            acc.applyCommand(Command.parseInput(x))
        }
        return finalPosition.horiz * finalPosition.depth
    }

    val testInput = readInput("day02", test = true)
    check(part1(testInput) == 150)

    val input = readInput("day02")
    println(part1(input))
    println(part2(input))
}
