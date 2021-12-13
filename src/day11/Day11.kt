package day11

import readInput
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int = run(input, 100).second

    fun part2(input: List<String>): Int {
        run(input, 500)
        return 0
    }

    val input = readInput("day11/Day11")
    println(part1(input))
    println(part2(input))
}

private fun run(input: List<String>, steps: Int): Pair<MutableList<Octopus>, Int> {

    val initialOctopuses = input.flatMapIndexed { rowIndex, row ->
        row.mapIndexed { columnIndex, element ->
            Octopus(
                id = row.length * (rowIndex) + columnIndex,
                x = rowIndex,
                y = columnIndex,
                energy = element.toString().toInt(),
                flashedInCurrentStep = false
            )
        }
    }
        .sortedBy { it.id }
        .toMutableList()

    return (0 until steps).fold(Pair(initialOctopuses, 0)) { acc: Pair<MutableList<Octopus>, Int>, step: Int ->
        val (octopuses, _) = acc
        octopuses
            .toList()
            .forEach { octopuses[it.id] = it.copy(energy = it.energy + 1) }

        while (octopuses.any { it.energy > 9 }) {
            octopuses.toList()
                .forEach {
                    when {
                        !it.flashedInCurrentStep && it.energy > 9 -> {
                            octopuses[it.id] = it.copy(energy = 0, flashedInCurrentStep = true)
                            octopuses.filter { octopus -> octopus.isNeighbourOf(it) }
                                .forEach { octopus ->
                                    octopuses[octopus.id] = octopus.copy(energy = octopus.energy + 1)
                                }
                        }
                    }
                }
        }
        if (octopuses.all { it.flashedInCurrentStep }) println("All octopuses flashed in step: ${step + 1}")
        Pair(
            octopuses.map { if (it.flashedInCurrentStep) it.copy(energy = 0, flashedInCurrentStep = false) else it }
                .toMutableList(),
            octopuses.count { it.flashedInCurrentStep } + acc.second
        )
    }
}

data class Octopus(
    val id: Int,
    val x: Int,
    val y: Int,
    val energy: Int,
    val flashedInCurrentStep: Boolean
) {
    fun isNeighbourOf(octopus: Octopus) =
        abs(octopus.x - x) <= 1 && abs(octopus.y - y) <= 1
}
