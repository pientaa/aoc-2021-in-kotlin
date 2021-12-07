fun main() {
    fun part1(input: List<String>): Int {
        return (0 until 80).fold(input.first().split(',').map { it.toInt() }) { acc: List<Int>, new: Int ->
            acc
                .flatMap { fishAge ->
                    when (fishAge) {
                        0 -> listOf(6, 8)
                        else -> listOf(fishAge - 1)
                    }
                }
        }
            .size
    }

    fun part2(input: List<String>): Long {
        val fishToCount = input.first().split(',').map { it.toInt() }
            .groupingBy { it }.eachCount().mapValues { it.value.toLong() }

        return (0 until (256)).fold(fishToCount) { acc: Map<Int, Long>, _: Int ->
            acc
                .flatMap { (fishAge, count) ->
                    when (fishAge) {
                        0 -> listOf(6 to count, 8 to count)
                        else -> listOf((fishAge - 1) to count)
                    }
                }
                .groupBy({ it.first }, { it.second })
                .mapValues { it.value.sum() }
        }.values.sum()
    }

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}