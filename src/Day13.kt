fun main() {

    fun IntBoard.foldX(leftLen: Int): IntBoard {
        val rightLen = this.width - leftLen - 1
        if (leftLen < rightLen) {
            error("can not fold by x $leftLen")
        }

        val foldedBoard = IntBoard(leftLen, height)

        /*
        width = 5
        x = 2
        leftlen = 2
        rightlen = 2
        0 1 2 3 4
        0 1
         */

        for(j in 0 until height) {
            for (i in 0 until leftLen) {
                val point = Point(i, j)
                foldedBoard[point] = this[point] + this[Point(width - i - 1, j)]
            }
        }

        return foldedBoard
    }

    fun IntBoard.foldY(topLen: Int): IntBoard {
        val bottomLen = this.height - topLen - 1
        if (topLen < bottomLen) {
            error("can not fold by y $topLen")
        }

        val foldedBoard = IntBoard(width, topLen)

        for(j in 0 until topLen) {
            for (i in 0 until width) {
                val point = Point(i, j)
                foldedBoard[point] = this[point] + this[Point(i, height - j - 1)]
            }
        }

        return foldedBoard
    }

    fun IntBoard.fold(point: Point): IntBoard = if (point.y == -1) {
        this.foldX(point.x)
    } else {
        this.foldY(point.y)
    }

    fun loadBoard(input: List<String>): IntBoard {
        val points = input.asSequence().filter { it.isNotEmpty() && it[0] != 'f' }
            .map {
                val coord = it.split(",")
                Point(coord[0].toInt(), coord[1].toInt())
            }.toList()
        val maxX = points.maxOf { it.x }
        val maxY = points.maxOf { it.y }
        val board = IntBoard(maxX + 1, maxY + 1)
        points.forEach { board[it] = 1 }
        return board
    }

    fun loadCommands(input: List<String>): List<Point> {
        val commands = input.asSequence().filter {
            it.isNotEmpty() && it[0] == 'f'
        }.map {
            val tokens = it.substringAfter("fold along ").split("=")
            when (tokens[0]) {
                "x" -> Point(tokens[1].toInt(), -1)
                "y" -> Point(-1, tokens[1].toInt())
                else -> error("bad tokens from $tokens")
            }
        }.toList()
        return commands
    }

    fun part1(input: List<String>): Int {

        val board = loadBoard(input)

        val commands = loadCommands(input)

        val foldedBoard = board.fold(commands.first())

        return foldedBoard.all().count { foldedBoard[it] > 0 }
    }

    fun part2(input: List<String>): String {

        val board = loadBoard(input)

        val commands = loadCommands(input)

        var foldedBoard = board
        for(command in commands) {
            foldedBoard = foldedBoard.fold(command)
        }

        foldedBoard.all().forEach { if (foldedBoard[it] > 0) foldedBoard[it] = 8 }

        return foldedBoard.display()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    val testPart1 = part1(testInput)
    check(testPart1 == 17) { "test part1 = $testPart1 "}

    val input = readInput("Day13")
    val part1 = part1(input)
    check(part1 == 678) { "part1 = $part1 "}
    println(part1)

    println(part2(testInput))

    println(part2(input))
}
