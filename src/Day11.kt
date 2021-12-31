fun main() {

    fun readyToFlash(value: Int) = value > 9

    fun step(board:IntBoard): Int {
        //First, the energy level of each octopus increases by 1.
        board.all().forEach { ++board[it] }

        //Then, any octopus with an energy level greater than 9 flashes. This increases the energy level of all
        // adjacent octopuses by 1, including octopuses that are diagonally adjacent. If this causes an octopus to
        // have an energy level greater than 9, it also flashes. This process continues as long as new octopuses keep
        // having their energy level increased beyond 9. (An octopus can only flash at most once per step.)

        val queue = ArrayDeque<Point>()
        val flashed = mutableSetOf<Point>()
        queue += board.all().filter { readyToFlash(board[it]) }

        while(queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (flashed.contains(current)) {
                continue
            } else {
                flashed += current
            }

            board.neighbours(current, 1).filter { !readyToFlash(board[it])}.forEach {
                ++board[it]
                if (readyToFlash(board[it])) {
                    queue += it
                }
            }
        }

        //Finally, any octopus that flashed during this step has its energy level set to 0, as it used all of its
        // energy to flash.
        flashed.asSequence().forEach { board[it] = 0 }

        return flashed.size
    }

    fun part1(input: List<String>): Int {

        val board = IntBoard.loadBoard(input)

        var sum = 0
        repeat(100) {
            sum += step(board)
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    val testPart1 = part1(testInput)
    check(testPart1 == 1656) {"test part1 $testPart1"}

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
