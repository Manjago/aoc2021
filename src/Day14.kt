fun main() {

    fun step(source: List<Char>, rules: Map<List<Char>, Char>): List<Char> {
        var firstEnter = true
        return source.asSequence().windowed(2).flatMap {
            if (firstEnter) {
                firstEnter = false
                if (rules.containsKey(it)) {
                    listOf(it[0], rules[it], it[1])
                } else {
                    it
                }
            } else {
                if (rules.containsKey(it)) {
                    listOf(rules[it], it[1])
                } else {
                    listOf(it[1])
                }
            }
        }.filterNotNull().toList()
    }

    fun makePolymer(input: List<String>, stepCount: Int): List<Char> {
        val template = input[0].toCharArray().toList()
        val rules = input.asSequence().drop(2).map {
            it.substringBefore(" -> ").toCharArray().toList() to it.substringAfter(" -> ")[0]
        }.toMap()

        var polymer = template
        var counter = 0
        repeat(stepCount) {
            ++counter
            println("before step $counter/$stepCount")
            polymer = step(polymer, rules)
            println("after step $counter/$stepCount")
        }
        return polymer
    }

    fun part1(input: List<String>): Int {

        val polymer = makePolymer(input, 10)

        val freq = polymer.groupingBy { it }.eachCount()
        val max = freq.maxOf { it.value }
        val min = freq.minOf { it.value }

        return max - min
    }

    fun part2(input: List<String>): Long {

        val polymer = makePolymer(input, 40)

        val freq = mutableMapOf<Char, Long>()
        for(char in polymer) {
            if (freq.containsKey(char)) {
                freq[char] = freq[char]!! + 1L
            } else {
                freq[char] = 1L
            }
        }

        val max = freq.maxOf { it.value }
        val min = freq.minOf { it.value }

        return max - min
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    val testPart1 = part1(testInput)
    check(testPart1 == 1588) { "test part1 = $testPart1 " }

    val input = readInput("Day14")
    val part1 = part1(input)
    check(part1 == 2851) { "part1 = $part1 " }
    println(part1)

    val testPart2 = part2(testInput)
    check(testPart2 == 2188189693529L) { "test part2 = $testPart2 " }

    println(part2(input))
}
