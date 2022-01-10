import java.math.BigInteger

fun main() {

    abstract class Snailfish

    data class SnailfishNumber(var value: BigInteger) : Snailfish() {
        override fun toString(): String {
            return value.toString()
        }
    }

    data class SnailfishPair(var parent: Snailfish?, var left: Snailfish?, var right: Snailfish?) : Snailfish() {
        override fun toString(): String {
            return "[${left?.toString()},${right?.toString()}]"
        }
        operator fun plus(item: SnailfishPair) : SnailfishPair {
            val result = SnailfishPair(null, this, item)
            result.parent = result
            this.parent = result
            item.parent = result
            return result
        }
    }

    fun parse(strValue: String): SnailfishPair {

        val head = SnailfishPair(null, null, null)
        head.parent = head
        var current: SnailfishPair? = null
        val sb = StringBuilder()

        strValue.toCharArray().asSequence().forEach {
            when {
                it == '[' -> {
                    when {
                        current == null -> {
                            current = head
                        }
                        current!!.left == null -> {
                            val newItem = SnailfishPair(current, null, null)
                            current!!.left = newItem
                            current = newItem
                        }
                        else -> {
                            val newItem = SnailfishPair(current, null, null)
                            current!!.right = newItem
                            current = newItem
                        }
                    }
                }
                it.isDigit() -> {
                    sb.append(it)
                }
                it == ',' -> {
                    if (sb.isNotEmpty()) {
                        val justNumber = sb.toString().toBigInteger()
                        sb.clear()
                        current!!.left = SnailfishNumber(justNumber)
                    }
                }
                it == ']' -> {
                    if (sb.isNotEmpty()) {
                        val justNumber = sb.toString().toBigInteger()
                        sb.clear()
                        current!!.right = SnailfishNumber(justNumber)
                    }
                    current = current!!.parent as SnailfishPair
                }

            }
        }
        return head
    }

    fun checkParse(strValue: String) {
        val parsed = parse(strValue)
        check(parsed.toString() == strValue)
        println("ok $parsed")
    }

    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    with(parse("[1,2]")) {
        with(this.left) {
            check(this is SnailfishNumber)
            check(this.value == BigInteger.ONE)
        }
        with(this.right) {
            check(this is SnailfishNumber)
            check(this.value == BigInteger.TWO)
        }
        check(this.toString() == "[1,2]")
        println("ok $this")
    }

    with(parse("[[1,2],3]")) {
        with(this.left) {
            check(this is SnailfishPair)
            with(this) {
                with(this.left) {
                    check(this is SnailfishNumber)
                    check(this.value == BigInteger.ONE)
                }
                with(this.right) {
                    check(this is SnailfishNumber)
                    check(this.value == BigInteger.TWO)
                }
            }
        }
        with(this.right) {
            check(this is SnailfishNumber)
            check(this.value == BigInteger.valueOf(3L))
        }
        check(this.toString() == "[[1,2],3]")
        println("ok $this")
    }

    with(parse("[9,[8,7]]")) {
        with(this.left) {
            check(this is SnailfishNumber)
            check(this.value == BigInteger.valueOf(9L))
        }
        with(this.right) {
            check(this is SnailfishPair)
            with(this) {
                with(this.left) {
                    check(this is SnailfishNumber)
                    check(this.value == BigInteger.valueOf(8L))
                }
                with(this.right) {
                    check(this is SnailfishNumber)
                    check(this.value == BigInteger.valueOf(7L))
                }
            }
        }
        check(this.toString() == "[9,[8,7]]")
        println("ok $this")
    }

    with(parse("[[1,9],[8,5]]")) {
        with(this.left) {
            check(this is SnailfishPair)
            with(this) {
                with(this.left) {
                    check(this is SnailfishNumber)
                    check(this.value == BigInteger.valueOf(1L))
                }
                with(this.right) {
                    check(this is SnailfishNumber)
                    check(this.value == BigInteger.valueOf(9L))
                }
            }
        }
        with(this.right) {
            check(this is SnailfishPair)
            with(this) {
                with(this.left) {
                    check(this is SnailfishNumber)
                    check(this.value == BigInteger.valueOf(8L))
                }
                with(this.right) {
                    check(this is SnailfishNumber)
                    check(this.value == BigInteger.valueOf(5L))
                }
            }
        }
        check(this.toString() == "[[1,9],[8,5]]")
        println("ok $this")
    }

    /*
       [
         [
           [
             [1,2],[3,4]
           ]
           ,
           [
             [5,6],[7,8]
           ]
         ]
       ,
       9]
     */
    checkParse("[[[[1,2],[3,4]],[[5,6],[7,8]]],9]")
    checkParse("[[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]")
    checkParse("[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]")

    val checkAddResult = parse("[1,2]") + parse("[[3,4],5]")
    check("[[1,2],[[3,4],5]]" == checkAddResult.toString())
}

/*
// test if implementation meets criteria from the description, like:
val testInput = readInput("Day18_test")
check(part1(testInput) == 1)

val input = readInput("Day18")
println(part1(input))
println(part2(input))
 */

