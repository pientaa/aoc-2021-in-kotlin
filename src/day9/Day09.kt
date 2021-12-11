package day9

import readInput
import java.lang.Math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val width = input.first().length

        val rows = input.map { it.map { it.toString().toInt() } }
            .map { row -> row.getAllLocalMinimums() }
        val columns = (0 until width).map { position ->
            input.map { it[position].toString().toInt() }
        }
            .map { column -> column.getAllLocalMinimums() }

        return rows.flatMapIndexed { rowIndex, row ->
            row.mapIndexed { columnIndex, element ->
                if (element.second && columns[columnIndex][rowIndex].second) element.first
                else null
            }
        }
            .mapNotNull { it }
            .sumOf { it + 1 }
    }

    fun part2(input: List<String>): Int {
        val rows = input.map { row ->
            row.map { it.toString().toInt() }
        }

        val points = rows.flatMapIndexed { rowIndex, row ->
            row.mapIndexed { columnIndex, element ->
                Triple(columnIndex, rowIndex, element)
            }
        }
            .filter { it.third != 9 }

        var clusterId = 0

        val initialClusters = points
            .map { ClusterPoint(it.first, it.second, it.third) }
            .foldIndexed(listOf()) { index: Int, acc: List<ClusterPoint>, point: ClusterPoint ->
                acc + (if (acc.isEmpty()) point.copy(clusterId = clusterId) else
                    acc.find {
                        (it.x == point.x && abs(point.y - it.y) == 1) ||
                                (it.y == point.y && abs(point.x - it.x) == 1)
                    }
                        ?.let {
                            point.copy(clusterId = it.clusterId)
                        }
                        ?: run {
                            clusterId += 1
                            point.copy(clusterId = clusterId)
                        })
            }
            .groupBy { it.clusterId }

        val alreadyValidClusters = initialClusters.filterNot { (index, clusterPoints) ->
            initialClusters.filterNot { it.key == index }
                .any {
                    it.value.any { point -> point.isNeighbourAnyOf(clusterPoints) }
                }
        }

        val mergedClusters = initialClusters
            .filterNot { it.key in alreadyValidClusters.keys }
            .map { it }
            .foldIndexed(mutableMapOf<Int, List<ClusterPoint>>()) { index, acc, entry ->
                if (index == 0) mutableMapOf(entry.key!! to entry.value)
                else {
                    val neighbours = acc
                        .filter { it.value.any { point -> point.isNeighbourAnyOf(entry.value) } }
                        .mapValues { it.value.map { it.copy(clusterId = entry.key) } }

                    neighbours.forEach {
                        acc.remove(it.key)
                    }
                    acc[entry.key!!] = (entry.value + neighbours.values.flatten()).distinctBy { Pair(it.x, it.y) }
                    acc
                }
            }

        return (alreadyValidClusters + mergedClusters)
            .values
            .flatten()
            .groupingBy { it.clusterId }
            .eachCount().values.sortedDescending()
            .take(3)
            .fold(0) { acc: Int, i: Int ->
                if (acc == 0) i else i * acc
            }
    }

    val input = readInput("day9/Day09")
    println(part1(input))
    println(part2(input))
}

data class ClusterPoint(
    val x: Int,
    val y: Int,
    val value: Int,
    val clusterId: Int? = null
) {
    fun isNeighbourAnyOf(clusterPoints: List<ClusterPoint>): Boolean =
        clusterPoints.any { point ->
            (this.x == point.x && abs(point.y - this.y) == 1) ||
                    (this.y == point.y && abs(point.x - this.x) == 1)
        }
}

private fun List<Int>.getAllLocalMinimums(): List<Pair<Int, Boolean>> {
    val width = this.size
    return this.mapIndexed { index, element ->
        when {
            index == 0 && element < this[index + 1] -> Pair(element, true)
            index == width - 1 && element < this[index - 1] -> Pair(element, true)
            index > 0 && index < width - 1 && element < this[index - 1] && element < this[index + 1] ->
                Pair(element, true)
            else -> Pair(element, false)
        }
    }
}

