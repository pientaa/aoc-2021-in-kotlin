package day7

import readInput
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {

        val crabsPositions = input.first().split(',').map { it.toInt() }
            .sorted()

        val desiredPosition = crabsPositions[crabsPositions.size / 2]

        return crabsPositions.sumOf { abs(it - desiredPosition) }
    }

    fun part2(input: List<String>): Int {
        val crabsPositions = input.first().split(',').map { it.toInt() }
            .sorted()

        return (0..2000).fold(1000000000) { acc: Int, new: Int ->
            val guess = crabsPositions.sumOf {
                val n = abs(it - new)
                (n * (n + 1)) / 2
            }
            if (guess < acc) guess else acc
        }
    }

    val input = readInput("day7/Day07")
    println(part1(input))
    println(part2(input))
}
