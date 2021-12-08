package day5

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.flatMap {
            val (first, second) = it.replace('>', ' ').split('-')
                .map { it.trim() }

            val (startX, startY) = first.split(',').map { it.toInt() }
            val (endX, endY) = second.split(',').map { it.toInt() }

            when {
                startX != endX && startY != endY -> emptyList()
                startX != endX -> {
                    (startX..endX)
                        .let { if (it.isEmpty()) (endX..startX) else it }
                        .map { Pair(it, startY) }
                }
                startY != endY -> {
                    (startY..endY)
                        .let { if (it.isEmpty()) (endY..startY) else it }
                        .map { Pair(startX, it) }
                }
                else -> emptyList()
            }
        }
            .groupingBy { it }
            .eachCount()
            .filterValues { it > 1 }
            .size
    }

    fun part2(input: List<String>): Int {
        return input.flatMap { input ->
            val (first, second) = input.replace('>', ' ').split('-')
                .map { it.trim() }

            val (startX, startY) = first.split(',').map { it.toInt() }
            val (endX, endY) = second.split(',').map { it.toInt() }
            when {
                startX != endX && startY != endY -> {
                    (startX..endX)
                        .let { if (it.isEmpty()) (endX..startX) else it }
                        .mapIndexed { index, _ ->
                            val stepX = if (startX > endX) -1 else 1
                            val stepY = if (startY > endY) -1 else 1
                            Pair(startX + (stepX * index), startY + (stepY * index))
                        }
                }
                startX != endX -> {
                    (startX..endX)
                        .let { if (it.isEmpty()) (endX..startX) else it }
                        .map { Pair(it, startY) }
                }
                startY != endY -> {
                    (startY..endY)
                        .let { if (it.isEmpty()) (endY..startY) else it }
                        .map { Pair(startX, it) }
                }
                else -> emptyList()
            }
        }
            .groupingBy { it }
            .eachCount()
            .filterValues { it > 1 }
            .size
    }

    val input = readInput("day5/Day05")
    println(part1(input))
    println(part2(input))
}
