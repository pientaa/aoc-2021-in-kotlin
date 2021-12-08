package day8

import readInput

fun main() {
    fun part1(input: List<String>): Int {

        return input.associate {
            val (input, output) = it.split('|').map { it.trim() }
            input to output
        }
            .mapValues {
                it.value.split(' ')
                    .count { signal ->
                        signal.length in listOf(2, 3, 4, 7)
                    }
            }
            .values.sum()
    }

    fun part2(rawInput: List<String>): Int {
        val foo = rawInput.map {
            val (input, output) = it.split('|').map { it.trim() }
            input.split(' ').map { it.toCharArray().sorted().joinToString("") } to
                    output.split(' ').map { it.toCharArray().sorted().joinToString("") }
        }

        return foo.sumOf { (input, output) ->
            val inputToSegments = input.groupBy { it.length }

            val one = inputToSegments[2]!!.first()
            val seven = inputToSegments[3]!!.first()
            val four = inputToSegments[4]!!.first()
            val eight = inputToSegments[7]!!.first()

            val cf = one
            val a = seven.filterNot { one.contains(it) }.first()
            val bd = four.filterNot { one.contains(it) }
            val eg = eight.filterNot { cf.contains(it) || it == a || bd.contains(it) }

            val six = inputToSegments[6]!!.first { command ->
                bd.all { command.contains(it) } &&
                        eg.all { command.contains(it) } && command.contains(a)
            }
            val nine = inputToSegments[6]!!.first { command ->
                bd.all { command.contains(it) } &&
                        cf.all { command.contains(it) } && command.contains(a)
            }

            val g = nine.filterNot { bd.contains(it) || cf.contains(it) || it == a }.first()
            val e = eg.first { it != g }

            val zero = inputToSegments[6]!!.first { command ->
                command != nine && command != six
            }

            val five = six.filterNot { it == e }

            val two = inputToSegments[5]!!.first { command ->
                command != five && command.contains(e)
            }

            val three = inputToSegments[5]!!.first { command ->
                command != five && command != two
            }

            output.joinToString("") {
                when (it) {
                    one -> "1"
                    two -> "2"
                    three -> "3"
                    four -> "4"
                    five -> "5"
                    six -> "6"
                    seven -> "7"
                    eight -> "8"
                    nine -> "9"
                    zero -> "0"
                    else -> throw Exception()
                }
            }.toInt()
        }
    }

    val input = readInput("day8/Day08")
    println(part1(input))
    println(part2(input))
}
