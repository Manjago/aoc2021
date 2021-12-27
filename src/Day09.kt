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
                if (
                    pretender < board.safeGetValue(x - 1, y, Int.MAX_VALUE) &&
                    pretender < board.safeGetValue(x + 1, y, Int.MAX_VALUE) &&
                    pretender < board.safeGetValue(x, y - 1, Int.MAX_VALUE) &&
                    pretender < board.safeGetValue(x, y + 1, Int.MAX_VALUE)
                        ) {
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
    println(part1(input))
    println(part2(input))
}
