typealias CoupleLetters = String
typealias Polymer = Map<CoupleLetters, Long>
typealias PolymerRules = Map<CoupleLetters, List<String>>

fun main() {

    fun polymerizationStep(source: Polymer, polymerRules: PolymerRules): Polymer {
      val answer = mutableMapOf<CoupleLetters, Long>()
      source.asSequence().forEach { pair ->
          val polymerRule = polymerRules[pair.key] ?: error("not found rule for ${pair.key}")

          polymerRule.forEach {
              if (answer.containsKey(it)) {
                  answer[it] = answer[it]!! + pair.value
              } else {
                  answer[it] = pair.value
              }
          }
      }
      return answer.toMap()
    }

    fun letterDistribution(polymer: Polymer) : Map<Char, Long> {
        val answer = mutableMapOf<Char, Long>()
        polymer.asSequence().forEach { pair ->
            pair.key.toCharArray().forEach {
                if (answer.containsKey(it)) {
                    answer[it] = answer[it]!! + pair.value
                } else {
                    answer[it] = pair.value
                }
            }
        }
        /*
        NNCB
        {N=1729, B=3497, C=596, H=322}

        C = 596 / 2 = 298
        B = (3497 + 1) / 2 = 1749 - because nncB
        N = (1729 + 1) /2 = 865 - because Nncb
        H = 322 / 2 = 161

         */
        return answer.asSequence().map {
            val newValue = if (it.value % 2 != 0L) {
                (it.value + 1) / 2
            } else {
                it.value / 2
            }
            it.key to newValue
        }.toMap()
    }

    fun makePolymer(input: List<String>, stepCount: Int): Polymer {

        var polymer = input[0].windowed(2).groupingBy { it }.eachCount().map {
            it.key to it.value.toLong()
        }.toMap()

        val polymerRules = input.asSequence().drop(2).map {
            val key = it.substringBefore(" -> ")
            val inserted = it.substringAfter(" -> ")
            key to listOf("${key[0]}$inserted", "$inserted${key[1]}")
        }.toMap()

        repeat(stepCount) {
            polymer = polymerizationStep(polymer, polymerRules)
        }
        return polymer
    }


    fun calcAnswer(polymer: Polymer): Long {
        val distribution = letterDistribution(polymer)
        val maxFreq = distribution.maxOf { it.value }
        val minFreq = distribution.minOf { it.value }

        return maxFreq - minFreq
    }

    fun part1(input: List<String>): Long = calcAnswer(makePolymer(input, 10))

    fun part2(input: List<String>): Long = calcAnswer(makePolymer(input, 40))

    val testInput = readInput("Day14_test")
    val testPart1 = part1(testInput)
    check(testPart1 == 1588L) { "test part1 = $testPart1 " }

    val input = readInput("Day14")
    val part1 = part1(input)
    check(part1 == 2851L) { "part1 = $part1 " }
    println(part1)

    val testPart2 = part2(testInput)
    check(testPart2 == 2188189693529L) { "test part2 = $testPart2 " }

    val part2 = part2(input)
    check(part2 == 10002813279337L) { "part2 = $part2 " }
    println(part2)
}
