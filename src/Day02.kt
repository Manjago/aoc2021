fun main() {
    fun part1(input: List<String>): Int {

        var h = 0
        var deep = 0

        for (rawString in input) {
            val split: List<String> = rawString.split(" ")
            val command = split[0]
            val value = split[1].toInt()

            when (command) {
                "forward" -> h += value
                "down" -> deep += value
                "up" -> deep -= value
                else -> throw IllegalArgumentException(command)
            }
        }


        return h * deep
    }

    fun part2(input: List<String>): Int {
        var h = 0
        var deep = 0
        var aim = 0

        for (rawString in input) {
            val split: List<String> = rawString.split(" ")
            val command = split[0]
            val value = split[1].toInt()

            when (command) {
                "forward" -> {
                    h += value
                    deep += aim * value
                }
                "down" -> aim += value
                "up" -> aim -= value
                else -> throw IllegalArgumentException(command)
            }
        }

        return h * deep
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
