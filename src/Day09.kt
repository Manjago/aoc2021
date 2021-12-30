fun main() {

    fun loadBoard(input: List<String>): IntBoard {
        val board = IntBoard(input[0].length, input.size)

        for (j in input.indices) {
            val row = input[j].toCharArray().asSequence().map { it.toString().toInt() }.toList().toIntArray()
            for (i in row.indices) {
                board[Point(i, j)] = row[i]
            }
        }
        return board
    }

    fun part1(input: List<String>): Int = with(loadBoard(input)) {
        all().sumOf { point ->
            val currentValue = this[point]
            if (!neighbours(point).any {
                    currentValue >= this[it]
                }) {
                currentValue + 1
            } else {
                0
            }
        }
    }

    fun basinCenters(board: IntBoard, seq: Sequence<Point>) : Sequence<Point> = seq.filter { point ->
        val currentValue = board[point]
        !board.neighbours(point).any {
            currentValue >= board[it]
        }
    }

    fun upperNeighbours(board: IntBoard, point: Point): Sequence<Point> {
        val currentValue = board[point]
        return board.neighbours(point).asSequence().filter {
            val value = board[it]
            value > currentValue && value != 9
        }
    }


    fun part2(input: List<String>): Int {

        val board = loadBoard(input)
        val sumList = mutableListOf<Int>()

        basinCenters(board, board.all()).forEach { basinCenter ->

            val queue = ArrayDeque<Point>()
            val visited = mutableSetOf<Point>()
            queue += basinCenter

            while(queue.isNotEmpty()) {
                 val item = queue.removeFirst()
                 visited.add(item)
                 queue += upperNeighbours(board, item)
            }

            sumList.add(visited.size)
        }

        sumList.sortBy { -it }
        return sumList.asSequence().take(3).reduce{ acc, item ->
            acc*item
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    val testPart1 = part1(testInput)
    check(testPart1 == 15) {"test part1 = $testPart1"}
    val testPart2 = part2(testInput)
    check(testPart2 == 1134) {"test part2 = $testPart2"}

    val input = readInput("Day09")
    val part1 = part1(input)
    check(part1 == 566) {"part1 = $part1"}
    println(part1)
    println(part2(input))
}
