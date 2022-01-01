fun main() {
    fun part1(input: List<String>): Int {

        val points = input.asSequence().filter { it.isNotEmpty() && it[0] != 'f' }
            .map {
                val coord = it.split(",")
                Point(coord[0].toInt(), coord[1].toInt())
            }.toList()
        val maxX = points.maxOf { it.x }
        val maxY = points.maxOf { it.y }
        val board = IntBoard(maxX + 1, maxY + 1)
        points.forEach { board[it] = 1 }

        val commands = input.asSequence().filter {
            it.isNotEmpty() && it[0] == 'f'
        }.map {
            val tokens = it.substringAfter("fold along ").split("=")
            when(tokens[0]) {
                "x" -> Point(tokens[1].toInt(), -1)
                "y" -> Point(-1, tokens[1].toInt())
                else -> error("bad tokens from $tokens")
            }
        }.toList()

        println(board.display())
        println(commands)

        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 1)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
