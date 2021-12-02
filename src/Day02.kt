fun main() {
    fun part1(input: List<String>): Int {
        return input.map {
            val (first, second) = it.split(" ")
            Pair(first, second)
                .toRelativeStep()
        }
            .fold(Step(0, 0)) { acc, new -> Step(acc.horizontal + new.horizontal, acc.depth + new.depth) }
            .let { it.horizontal * it.depth }
    }

    fun part2(input: List<String>): Int {
        return input.map {
            val (first, second) = it.split(" ")
            Pair(first, second)
        }
            .fold(AimedStep(0, 0, 0)) { acc, new ->
                val direction = new.first.uppercase()
                val step = new.second.toInt()

                when (Command.valueOf(direction)) {
                    Command.FORWARD -> AimedStep(
                        horizontal = acc.horizontal + step,
                        depth = acc.depth + (acc.aim * step),
                        aim = acc.aim
                    )
                    Command.UP -> AimedStep(horizontal = acc.horizontal, depth = acc.depth, aim = acc.aim - step)
                    Command.DOWN -> AimedStep(horizontal = acc.horizontal, depth = acc.depth, aim = acc.aim + step)
                }
            }
            .let { it.horizontal * it.depth }
    }

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

fun Pair<String, String>.toRelativeStep(): Step =
    when (Command.valueOf(this.first.uppercase())) {
        Command.FORWARD -> Step(second.toInt(), 0)
        Command.UP -> Step(0, -second.toInt())
        Command.DOWN -> Step(0, second.toInt())
    }

data class Step(
    val horizontal: Int,
    val depth: Int
)

data class AimedStep(
    val horizontal: Int,
    val depth: Int,
    val aim: Int
)

enum class Command {
    FORWARD,
    UP,
    DOWN
}