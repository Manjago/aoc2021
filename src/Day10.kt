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

    fun asIncompleteStack(command: String) : ArrayDeque<Char>? {
        val stack = ArrayDeque<Char>()

        command.toCharArray().forEach {
            when (it) {
                '(', '[', '{', '<' -> stack += it
                ')' -> if (stack.isEmpty() || stack.last() != '(') {
                    return null
                } else {
                    stack.removeLast()
                }
                '}' -> if (stack.isEmpty() || stack.last() != '{') {
                    return null
                } else {
                    stack.removeLast()
                }
                ']' -> if (stack.isEmpty() || stack.last() != '[') {
                    return null
                } else {
                    stack.removeLast()
                }
                '>' -> if (stack.isEmpty() || stack.last() != '<') {
                    return null
                } else {
                    stack.removeLast()
                }
                else -> error("bad char $it")
            }
        }

        return if (stack.isEmpty()) {
            null
        } else {
            stack
        }
    }

    fun completeSeq(stack: ArrayDeque<Char>) : List<Char> {

        val result = mutableListOf<Char>()
        while(stack.isNotEmpty()) {
            val char = stack.removeLast()
            result += when(char) {
                '(' -> ')'
                '[' -> ']'
                '{' -> '}'
                '<' -> '>'
                else -> error("bad char $char")
            }
        }

        return result.toList()
    }

    fun costCompletingChar(char: Char): Int = when(char) {
        ')' -> 1
        ']' -> 2
        '}' -> 3
        '>' -> 4
        else -> error("bad char $char")
    }

    fun calcIncompleteCost(command: String) : Long {
        val stack = asIncompleteStack(command) ?: return 0
        val list = completeSeq(stack)

        var result = 0L
        for(char in list) {
            result = result * 5 + costCompletingChar(char)
        }

        return result
    }

    fun part1(input: List<String>): Int = input.asSequence().map {
        costOfChar(firstIllegalChar(it))
    }.sum()

    fun part2(input: List<String>): Long {
        val pretenders = input.asSequence().map {
            calcIncompleteCost(it)
        }.filter { it != 0L }.sorted().toList()

        val size = pretenders.size
        check(size % 2  != 0) {"size must be odd, but size is $size"}
        val needDrop = (size - 1) / 2

        val result = pretenders.asSequence().drop(needDrop).take(1).toList()
        return result[0]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    val testPart1 = part1(testInput)
    check(testPart1 == 26397) {"test part1 = $testPart1"}
    val testPart2 = part2(testInput)
    check(testPart2 == 288957L) {"test part2 = $testPart2"}

    val input = readInput("Day10")
    val part1 = part1(input)
    check(part1 == 388713) {"part1 = $part1"}
    println(part2(input))

        // 87613442 too low
}
