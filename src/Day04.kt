fun main() {
    val bingoSize = 5

    fun part1(input: List<String>): Int {
        val bingoNumbers = generateBingoNumbers(input)
        val bingoBoards = generateBingoBoards(input, bingoSize)

        return (bingoNumbers.indices)
            .firstNotNullOf { index ->
                val currentBingoNumbers = bingoNumbers.subList(0, index + 1)
                bingoBoards.firstOrNull { it.isAnyRowOrColumnFullyGuessed(currentBingoNumbers) }
                    ?.let { Pair(currentBingoNumbers, it) }
            }
            .let { it.second.calculateScore(it.first) }
    }

    fun part2(input: List<String>): Int {
        val bingoNumbers = generateBingoNumbers(input)
        val bingoBoards = generateBingoBoards(input, bingoSize)

        return bingoBoards.toMutableList().run {
            bingoNumbers.indices.map { index ->
                val currentBingoNumbers = bingoNumbers.subList(0, index + 1)
                if (this.size != 1) {
                    this.removeAll { it.isAnyRowOrColumnFullyGuessed(currentBingoNumbers) }
                    null
                } else return@map Pair(currentBingoNumbers, this.first())
            }
                .filterNotNull()
                .first()
        }
            .let { it.second.calculateScore(it.first) }
    }

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

private fun generateBingoBoards(input: List<String>, bingoSize: Int): List<BingoBoard> =
    input.drop(1)
        .map { line ->
            line.split(' ')
        }
        .map { it.filter { it.isNotEmpty() } }
        .filter { it.size == bingoSize }
        .toBingoMatrices()

private fun generateBingoNumbers(input: List<String>): List<Int> = input.first().split(',').map { it.toInt() }

private fun List<List<String>>.toBingoMatrices() =
    this.windowed(5, 5)
        .map { window -> BingoBoard(window.map { windowRow -> windowRow.map { it.toInt() } }) }

data class BingoBoard(
    val rows: List<List<Int>>,
) {

    override fun toString(): String = "\n${rows[0]}\n${rows[1]}\n${rows[2]}\n${rows[3]}\n${rows[4]}\n"

    private val columns = listOf(
        rows.map { it[0] },
        rows.map { it[1] },
        rows.map { it[2] },
        rows.map { it[3] },
        rows.map { it[4] },
    )

    fun calculateScore(numbers: List<Int>): Int =
        rows.flatMap { it.filterNot { numbers.contains(it) } }
            .sum() * numbers.last()

    fun isAnyRowOrColumnFullyGuessed(numbers: List<Int>) =
        rows.any { rowNumbers ->
            rowNumbers.all {
                numbers.contains(it)
            }
        } ||
                columns.any { columnNumbers ->
                    columnNumbers.all {
                        numbers.contains(it)
                    }
                }
}