fun main() {
    fun part1(input: List<String>): Int {

        val board = IntBoard.loadBoard(input)


        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day15_test")
    val testPart1 = part1(testInput)
    check(testPart1 == 40) {"test part1 = $testPart1"}

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}
