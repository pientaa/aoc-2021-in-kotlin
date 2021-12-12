package day10

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return checkLinesCorrectness(input)
            .sumOf { it.second }
    }

    fun part2(input: List<String>): Long {
        val allValues = checkLinesCorrectness(input)
            .asSequence()
            .filter { it.second == 0 }
            .map { it.first }
            .map {
                it.foldRightIndexed(0L) { index: Int, c: Char, acc: Long ->
                    when (c) {
                        '(' -> 1L
                        '[' -> 2L
                        '{' -> 3L
                        '<' -> 4L
                        else -> throw Exception()
                    }
                        .let { acc * 5L + it }
                }
            }
            .sorted()
            .toList()

        return allValues[allValues.size / 2]
    }

    val input = readInput("day10/Day10")
    println(part1(input))
    println(part2(input))
}

private fun checkLinesCorrectness(input: List<String>) = input.map { line ->
    line
        .foldIndexed(Pair("", 0)) { index: Int, acc: Pair<String, Int>, c: Char ->
            val (lastOpened, _) = acc

            when {
                index == 0 -> Pair(c.toString(), 0)
                c.isOpeningBracet() -> Pair(lastOpened + c, 0)
                lastOpened.isEmpty() && c.isClosingBracet() -> return@foldIndexed Pair("", c.toPoints())
                c.isClosingBracet() && lastOpened.last().isClosedBy(c) -> Pair(lastOpened.dropLast(1), 0)
                c.isClosingBracet() && !lastOpened.last().isClosedBy(c) ->
                    return@map Pair(
                        c.takeIf { it.isOpeningBracet() }.let { lastOpened + (it ?: "") },
                        c.toPoints()
                    )
                else -> throw Exception()
            }
        }
}

private fun Char.isClosedBy(c: Char): Boolean = when (this) {
    '(' -> c == ')'
    '[' -> c == ']'
    '{' -> c == '}'
    '<' -> c == '>'
    else -> throw Exception()
}

private fun Char.isClosingBracet(): Boolean = this in listOf(')', ']', '}', '>')
private fun Char.isOpeningBracet() = this in listOf('(', '[', '{', '<')

private fun Char.toPoints() = when (this) {
    ')' -> 3
    ']' -> 57
    '}' -> 1197
    '>' -> 25137
    else -> throw Error()
}