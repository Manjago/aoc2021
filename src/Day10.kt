fun main() {

    fun firstIllegalChar(command: String) : Char? {

        val stack = ArrayDeque<Char>()

        command.toCharArray().forEach {
             when (it) {
                 '(', '[', '{', '<' -> stack += it
                 ')' -> if (stack.isEmpty() || stack.last() != '(') {
                     return it
                 } else {
                     stack.removeLast()
                 }
                 '}' -> if (stack.isEmpty() || stack.last() != '{') {
                     return it
                 } else {
                     stack.removeLast()
                 }
                 ']' -> if (stack.isEmpty() || stack.last() != '[') {
                     return it
                 } else {
                     stack.removeLast()
                 }
                 '>' -> if (stack.isEmpty() || stack.last() != '<') {
                     return it
                 } else {
                     stack.removeLast()
                 }
                 else -> error("bad char $it")
             }

        }

        return null
    }

    fun costOfChar(char: Char?) : Int = when (char) {
        null -> 0
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> error("bad char in cost $char")
    }

    fun part1(input: List<String>): Int = input.asSequence().map {
        costOfChar(firstIllegalChar(it))
    }.sum()

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    val testPart1 = part1(testInput)
    check(testPart1 == 26397) {"test part1 = $testPart1"}

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
