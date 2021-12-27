fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        input.forEach {
            val count = it.substringAfter("| ", "").split(" ")
                .count { it.length == 2 || it.length == 4 || it.length == 7 || it.length == 3 }
            sum += count
        }

        return sum
    }

    data class SevenDigit(val segments: Set<Char>) {
        operator fun plus(other: SevenDigit): SevenDigit = SevenDigit(this.segments.intersect(other.segments))
        fun size() = segments.size
    }

    fun getDict(digits: List<SevenDigit>): Map<SevenDigit, Int> {
        val dict = Array<SevenDigit?>(10) { null }
        dict[1] = digits.first { it.size() == 2 }
        dict[4] = digits.first { it.size() == 4 }
        dict[7] = digits.first { it.size() == 3 }
        dict[8] = digits.first { it.size() == 7 }
        dict[6] = digits.asSequence().filter { it.size() == 6 }
            .first {
                (dict[1]!! + it).size() == 1
            }
        dict[0] = digits.asSequence().filter {
            it.size() == 6 && it != dict[6]
        }.first { (dict[4]!! + it).size() == 3 }
        dict[9] = digits.asSequence().filter { it.size() == 6 && it != dict[6] && it != dict[0] }
            .first()
        dict[3] = digits.asSequence().filter { it.size() == 5 }
            .first {
                (dict[1]!! + it).size() == 2
            }
        dict[5] = digits.asSequence().filter { it.size() == 5 && it != dict[3] }
            .first {
                (dict[6]!! + it) == it
            }
        dict[2] = digits.asSequence().filter { it.size() == 5 && it != dict[3] && it != dict[5] }
            .first()

        val dictMap = mutableMapOf<SevenDigit, Int>()
        for (i in dict.indices) {
            dictMap[dict[i]!!] = i
        }
        return dictMap.toMap()
    }

    fun part2(input: List<String>): Int {

        var sum = 0
        input.forEach { inputString ->
            val digits = inputString.substringBefore(" | ").split(" ").map {
                SevenDigit(it.toCharArray().toSet())
            }
            val dict = getDict(digits)
            val answers = inputString.substringAfter(" | ").split(" ").map {
                SevenDigit(it.toCharArray().toSet())
            }

            sum += dict[answers[0]]!! * 1000 + dict[answers[1]]!! * 100 + dict[answers[2]]!! * 10 + dict[answers[3]]!!
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    val testPart1 = part1(testInput)
    check(testPart1 == 26) { "test part1 = $testPart1" }
    val testPart2 = part2(testInput)
    check(testPart2 == 61229) { "test part2 = $testPart2" }

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
