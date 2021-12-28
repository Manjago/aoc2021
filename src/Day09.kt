fun main() {

    fun part1(input: List<String>): Int {

        val board = IntBoard(input[0].length, input.size)

        for(j in input.indices) {
            val row = input[j].toCharArray().asSequence().map { it.toString().toInt() }.toList().toIntArray()
            for(i in row.indices) {
                board.setValue(i, j, row[i])
            }
        }

        var sum = 0
        for(y in 0 until board.height) {
            for (x in 0 until  board.width) {

                val pretender = board.getValue(x, y)

                if (!board.neighbours(x, y).any {
                        pretender >= board[it]
                    }) {
                    sum += pretender + 1
                }
            }
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    val testPart1 = part1(testInput)
    check(testPart1 == 15) {"test part1 = $testPart1"}

    val input = readInput("Day09")
    val part1 = part1(input)
    check(part1 == 566) {"part1 = $part1"}
    println(part1)
    println(part2(input))
}
