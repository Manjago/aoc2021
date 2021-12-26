fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        input.forEach {
            val count = it.substringAfter("| ", "").split(" ").asSequence()
                .count { it.length == 2 || it.length == 4 || it.length == 7 || it.length == 3 }
            sum += count
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    val testPart1 = part1(testInput)
    check(testPart1 == 26) {"test part1 = $testPart1"}

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
