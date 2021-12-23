fun main() {
    fun part1(input: List<String>): Int {

        val maxLen = input[0].length

        val bit1 = IntArray(maxLen)
        val bit0 = IntArray(maxLen)
        for(rawString in input) {
            val rawArray = rawString.toCharArray()
            for (i in 0 until maxLen) {
                if (rawArray[i] == '1') {
                    ++bit1[i]
                } else {
                    ++bit0[i]
                }
            }
        }

        val gamma = StringBuilder()
        val epsilon = StringBuilder()

        for (i in 0 until maxLen) {
            if (bit1[i] >= bit0[i]) {
                gamma.append('1')
                epsilon.append('0')
            } else {
                gamma.append('0')
                epsilon.append('1')
            }
        }

        val gammaInt = gamma.toString().toInt(2)
        val epsilonInt = epsilon.toString().toInt(2)

        return gammaInt * epsilonInt
    }

    fun mostCommonBit(pos: Int, input: List<String>): Char {
        var bit1 = 0
        var bit0 = 0
        for(rawString in input) {
            if (rawString[pos] == '1') {
                ++bit1
            } else {
                ++bit0
            }
        }
        return if (bit1 >= bit0) '1' else '0'
    }

    fun leastCommonBit(pos: Int, input: List<String>): Char = if (mostCommonBit(pos, input) == '1') '0' else '1'

    fun filterByBit(bit: Char, pos: Int, input: List<String>) : List<String> = input.asSequence().filter {
        it[pos] == bit
    }.toList()

    fun calcMetric(input: List<String>, selectorCalcer: (Int, List<String>) -> Char) : Int {
        var data = input
        var pos = 0
        while(data.size != 1) {
            val selector = selectorCalcer(pos, data)
            data = filterByBit(selector, pos++, data)
        }

        return data[0].toInt(2)
    }

    fun part2(input: List<String>): Int = calcMetric(input, ::mostCommonBit) *
            calcMetric(input, ::leastCommonBit)


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
