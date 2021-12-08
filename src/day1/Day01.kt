package day1

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val firstMeasurement = input.first().toInt()
        return input.fold(Pair(firstMeasurement, 0)) { acc: Pair<Int, Int>, new: String ->
            new.toInt()
                .let {
                    if (it > acc.first) Pair(it, acc.second + 1)
                    else Pair(it, acc.second)
                }
        }.second
    }

    fun part2(input: List<String>): Int {
        return input.windowed(size = 3, step = 1)
            .map {
                it.fold(0) { acc: Int, new: String ->
                    acc + new.toInt()
                }
            }
            .foldIndexed(Pair(0, 0)) { index: Int, acc: Pair<Int, Int>, new: Int ->
                when {
                    index == 0 -> Pair(new, 0)
                    new > acc.first -> Pair(new, acc.second + 1)
                    else -> Pair(new, acc.second)
                }
            }.second
    }

    val input = readInput("day1/Day01")
    println(part1(input))
    println(part2(input))
}
