import java.math.BigInteger

typealias SnailfishId=BigInteger

fun main() {

    var idSequence = BigInteger.ZERO

    abstract class Snailfish(val id: SnailfishId = idSequence++)

    data class SnailfishNumber(var value: BigInteger) : Snailfish() {
        override fun toString(): String {
            return value.toString()
        }
    }

     class SnailfishPair(var parent: Snailfish?, var left: Snailfish?, var right: Snailfish?) : Snailfish() {
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

         override fun equals(other: Any?): Boolean {
             if (this === other) return true
             if (javaClass != other?.javaClass) return false

             other as SnailfishPair

             return other.toString() == toString()
         }

         override fun hashCode(): Int = toString().hashCode()
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

    fun String.toPair() = parse(this)

    fun explodePretender(root: SnailfishPair, level: Int) : SnailfishPair? {

        if (level == 4) {
            return root
        }

        if (root.left != null && root.left is SnailfishPair) {
            return explodePretender(root.left as SnailfishPair, level + 1)
        }
        if (root.right != null && root.right is SnailfishPair) {
            return explodePretender(root.right as SnailfishPair, level + 1)
        }

        return null
    }

    fun numerate(root: SnailfishPair) : Map<Int, Snailfish> {

        val visited = mutableSetOf<SnailfishId>()
        val queue = ArrayDeque<SnailfishPair>()
        val result = mutableMapOf<Int, Snailfish>()
        var counter = 0

        queue += root

        while(queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (visited.contains(current.id)) {
                continue
            }
            visited += current.id

            when {
                current.left != null && current.left is SnailfishNumber -> result[counter++] = current.left as SnailfishNumber
                current.left != null && current.left is SnailfishPair -> queue += current.left as SnailfishPair
            }
            when {
                current.right != null && current.right is SnailfishNumber -> result[counter++] = current.right as SnailfishNumber
                current.right != null && current.right is SnailfishPair -> queue += current.right as SnailfishPair
            }

        }

        return result.toMap()
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

    check(explodePretender(parse("[[1,9],[8,5]]"), 0) == null)
    check(explodePretender("[[[[[9,8],1],2],3],4]".toPair(), 0) == "[9,8]".toPair())
    check(explodePretender("[7,[6,[5,[4,[3,2]]]]]".toPair(), 0) == "[3,2]".toPair())
    check(explodePretender("[[6,[5,[4,[3,2]]]],1]".toPair(), 0) == "[3,2]".toPair())
    check(explodePretender("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]".toPair(), 0) == "[7,3]".toPair())

    check(numerate(parse("[[1,9],[8,5]]")).toString() == "{0=1, 1=9, 2=8, 3=5}")
    println(numerate("[[[[[9,8],1],2],3],4]".toPair()))
}

/*
// test if implementation meets criteria from the description, like:
val testInput = readInput("Day18_test")
check(part1(testInput) == 1)

val input = readInput("Day18")
println(part1(input))
println(part2(input))
 */

