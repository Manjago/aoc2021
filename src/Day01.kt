fun main() {
    fun part1(input: List<String>): Int {

        var firstEnter = true
        var incCounter = 0
        var prevValue = 0
        for (strNumber in input) {
            if (firstEnter) {
                prevValue = strNumber.toInt()
                firstEnter = false
            } else {
                val currentValue = strNumber.toInt()
                if (currentValue > prevValue) {
                    ++incCounter
                }
                prevValue = currentValue
            }
        }

        return incCounter
    }

    fun part2(input: List<String>): Int {

        if (input.size < 3) {
            return -1
        }

        var prevWindow = input[0].toInt() + input[1].toInt() + input[2].toInt()
        var incCounter = 0

        for (i in 3 until input.size) {
            val currentWindow = prevWindow + input[i].toInt() - input[i- 3].toInt()
            if (currentWindow > prevWindow) {
                ++incCounter
            }
            prevWindow = currentWindow
        }

        return incCounter
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
