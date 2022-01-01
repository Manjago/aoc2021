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

    fun part1(input: List<String>): Int {

        val template = input[0].toCharArray().toList()
        val rules = input.asSequence().drop(2).map {
            it.substringBefore(" -> ").toCharArray().toList() to it.substringAfter(" -> ")[0]
        }.toMap()

        var polymer = template
        repeat(10) {
            polymer = step(polymer, rules)
        }

        val freq = polymer.groupingBy { it }.eachCount()
        val max = freq.maxOf { it.value }
        val min = freq.minOf { it.value }

        return max - min
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    val testPart1 = part1(testInput)
    check(testPart1 == 1588) { "test part1 = $testPart1 " }

    val input = readInput("Day14")
    val part1 = part1(input)
    check(part1 == 2851) { "part1 = $part1 " }
    println(part1)
    println(part2(input))
}
