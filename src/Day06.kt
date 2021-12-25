fun main() {

    val limit = 80

    fun part1(input: List<String>): Int {

        val data = input[0].split(",").asSequence().map { it.toInt() }.toMutableList()

        var turn = 0
        while(true) {

            val finish = data.size
            for(i in 0 until finish) {
                val item = data[i]
                if (item == 0) {
                    data[i] = 6
                    data.add(8)
                } else {
                    data[i] = item - 1
                }
            }

            ++turn
            if (turn == limit) {
                break
            }
        }

        return data.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    val part1 = part1(testInput)
    check(part1 == 5934) {"test part1 = $part1"}

    val input = readInput("Day06")
    val realPart1 = part1(input)
    check(realPart1 == 349549) {"real part1 = $realPart1"}
    println("part2 = " + part2(input))
}
