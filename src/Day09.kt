fun main() {

    fun inBound(x: Int, bound: Int) : Boolean = x in 0 until bound

    fun isLessThan(data: Array<IntArray>, width: Int, height: Int, x: Int, y: Int, pretender: Int) : Boolean =
        if (inBound(x, width) && inBound(y, height)) {
            pretender < data[y][x]
        } else {
            true
        }

    fun part1(input: List<String>): Int {

        val width = input[0].length
        val height = input.size

        val data = Array(height) {IntArray(width)}
        for(i in input.indices) {
            data[i] = input[i].toCharArray().asSequence().map { it.toString().toInt() }.toList().toIntArray()
        }

        var sum = 0
        for(y in 0 until height) {
            for (x in 0 until  width) {

                val pretender = data[y][x]
                if (
                    isLessThan(data, width, height, x - 1, y, pretender) &&
                    isLessThan(data, width, height, x + 1, y , pretender) &&
                    isLessThan(data, width, height, x, y - 1, pretender) &&
                    isLessThan(data, width, height, x, y + 1, pretender)
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
