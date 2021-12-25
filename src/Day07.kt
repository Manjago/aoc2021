import kotlin.math.abs

fun main() {

    fun cost(data: List<Int>, pos: Int): Int = data.sumOf { abs(pos - it) }

    fun fuelByStep(steps: Int): Int {
        var sum = 0
        var inc = 1
        var currentDist = steps
        while(currentDist > 0) {
            --currentDist
            sum += inc
            ++inc
        }
        return sum
    }

    fun costAdvanced(data: List<Int>, pos: Int): Int = data.sumOf {
        fuelByStep(abs(pos - it))
    }

    fun core(data: List<Int>, calcer: (List<Int>, Int) -> Int): Int {
        var pretender = Integer.MAX_VALUE
        for (i in data.indices) {
            pretender = pretender.coerceAtMost(calcer(data, i))
        }
        return pretender
    }

    fun part1(input: List<String>): Int {
        val data = stringAsInts(input[0])
        return core(data, ::cost)
    }

    fun part2(input: List<String>): Int {
        val data = stringAsInts(input[0])
        return core(data, ::costAdvanced)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    val realPart1 = part1(input)
    check(realPart1 == 355521)
    println(part2(input))
}
