fun main() {

    fun optimalSolution(input: List<String>, limit: Int): Long {
        val age = LongArray(9)
        input[0].split(",").asSequence().map { it.toInt() }.forEach {
            ++age[it]
        }

        repeat(limit) {
            val temp = age[0]
            for(i in 0..7) {
                age[i] = age[i+1]
            }
            age[8] = temp
            age[6] += temp
        }

        return age.asSequence().sum()
    }

    fun naiveSolution(input: List<String>, limit: Int): Int {
        val data = input[0].split(",").asSequence().map { it.toInt() }.toMutableList()

        var turn = 0
        while (true) {

            val finish = data.size
            for (i in 0 until finish) {
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

    fun part1(input: List<String>): Long {
        return optimalSolution(input, 80)
    }

    fun part2(input: List<String>): Long {
        return optimalSolution(input, 256)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    val part1 = part1(testInput)
    check(part1 == 5934L) {"test part1 = $part1"}
    val part2 = part2(testInput)
    check(part2 == 26984457539L) {"test part2 = $part2"}

    val input = readInput("Day06")
    val realPart1 = part1(input)
    check(realPart1 == 349549L) {"real part1 = $realPart1"}
    println("real part2 = " + part2(input))
}
