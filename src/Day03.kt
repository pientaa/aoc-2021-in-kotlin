import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        val size = input.first().length

        val gammaRate = input.fold(List(size) { 0 }) { acc: List<Int>, new: String ->
            List(size) { index ->
                acc[index] + new[index].toString().toInt()
            }
        }
            .map { it > input.size / 2 }

        val epsilonRate = gammaRate
            .map { !it }

        return gammaRate.toInteger() * epsilonRate.toInteger()
    }

    fun part2(input: List<String>): Int {
        return Rating.OXYGEN_GENERATOR_RATING.getValueFor(input) * Rating.CO2_SCRUBBER_RATING.getValueFor(input)
    }

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

private fun Rating.getValueFor(input: List<String>) = (0 until input.first().length)
    .fold(input) { acc: List<String>, bitIndex: Int ->
        if (acc.size == 1) return@fold acc
        acc.filterOutByBitIndex(bitIndex, getOccurrence())
    }
    .first()
    .map { it == '1' }
    .toInteger()

private fun List<String>.filterOutByBitIndex(bitIndex: Int, occurrence: BitOccurrence) =
    getBitBy(bitIndex, occurrence).let { dominantBit ->
        this.filter {
            it[bitIndex] == dominantBit
        }
    }

private fun List<String>.getBitBy(bitIndex: Int, occurrence: BitOccurrence) =
    when (occurrence) {
        BitOccurrence.MOST_COMMON -> if (countPositiveBits(bitIndex) >= this.size / 2.0) '1' else '0'
        BitOccurrence.LEAST_COMMON -> if (countPositiveBits(bitIndex) >= this.size / 2.0) '0' else '1'

    }

private fun List<String>.countPositiveBits(bitIndex: Int) =
    fold(0) { acc: Int, new: String ->
        acc + new[bitIndex].toString().toInt()
    }

private fun Rating.getOccurrence() = when (this) {
    Rating.OXYGEN_GENERATOR_RATING -> BitOccurrence.MOST_COMMON
    Rating.CO2_SCRUBBER_RATING -> BitOccurrence.LEAST_COMMON
}

enum class BitOccurrence {
    MOST_COMMON,
    LEAST_COMMON
}

enum class Rating {
    OXYGEN_GENERATOR_RATING,
    CO2_SCRUBBER_RATING
}

private fun List<Boolean>.toInteger(): Int =
    this
        .reversed()
        .foldIndexed(0) { index: Int, acc: Int, new: Boolean ->
            if (new) acc + 2.0.pow(index).toInt() else acc
        }
