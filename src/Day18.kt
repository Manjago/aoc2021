import java.math.BigInteger

fun main() {

    abstract class Snailfish

    data class SnailfishNumber(var value: BigInteger) : Snailfish() {
        override fun toString(): String {
            return value.toString()
        }
    }

    data class SnailfishPair(var left: Snailfish?, var right: Snailfish?) : Snailfish() {
        override fun toString(): String {
            return "[${left?.toString()},${right?.toString()}]"
        }
    }

    fun parse(strValue: String): Snailfish {

        val stack = ArrayDeque<SnailfishPair>()
        val currentNumber = StringBuilder()

        strValue.toCharArray().asSequence().forEach {
            when {
                it == '[' -> stack += SnailfishPair(null, null)
                it.isDigit() -> currentNumber.append(it)
                it == ',' -> {
                    stack.last().left = SnailfishNumber(currentNumber.toString().toBigInteger())
                    currentNumber.clear()
                }
                it == ']' -> {
                    stack.last().right = SnailfishNumber(currentNumber.toString().toBigInteger())
                    currentNumber.clear()
                }
            }
        }
        return stack.removeFirst()
    }

    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    with(parse("[1,2]")) {
        check(this is SnailfishPair)
        with(this.left) {
            check(this is SnailfishNumber)
            check(this.value == BigInteger.ONE)
        }
        with(this.right) {
            check(this is SnailfishNumber)
            check(this.value == BigInteger.TWO)
        }
        println("ok $this")
    }

/*
// test if implementation meets criteria from the description, like:
val testInput = readInput("Day18_test")
check(part1(testInput) == 1)

val input = readInput("Day18")
println(part1(input))
println(part2(input))
 */
}
