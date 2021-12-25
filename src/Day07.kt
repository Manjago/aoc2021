import java.lang.Math.abs

fun main() {

    fun cost(data: List<Int>, pos: Int): Int = data.sumOf { abs(pos - it) }

    fun part1(input: List<String>): Int {
        val data = input[0].split(",").asSequence().map { it.toInt() }.toList()
        var pretender = Integer.MAX_VALUE
        for (i in data.indices) {
            pretender = pretender.coerceAtMost(cost(data, i))
        }
        return pretender
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)

    val input = readInput("Day07")
    val realPart1 = part1(input)
    check(realPart1 == 355521)
    println(part2(input))
}
