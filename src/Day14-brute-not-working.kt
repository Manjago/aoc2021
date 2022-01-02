fun main() {

    fun stepOptimal(source: Node<Char>, rule: (Char,Char) -> Char?) {
         var pointer = source
         var next = pointer.next
         while(next != null) {
            val charToInsert = rule(pointer.value, next.value)
            if (charToInsert != null) {
                val inserted = Node(charToInsert, next)
                pointer.next = inserted
            }
            pointer = next
            next = next.next
        }
    }

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

    fun makePolymerOptimal(input: List<String>, stepCount: Int): Node<Char> {
        val head = Node('z', null)
        var pointer = head
        input[0].toCharArray().forEach {
            pointer.next = Node(it, null)
            pointer = pointer.next!!
        }
        val polymer = head.next!!

        val rules = input.asSequence().drop(2).map {
            val result = CharArray(3)
            val key = it.substringBefore(" -> ").toCharArray()
            val value = it.substringAfter(" -> ")[0]
            result[0] = key[0]
            result[1] = key[1]
            result[2] = value
            result
        }.toList()

        repeat(stepCount) {
            stepOptimal(polymer) { char, charNext ->

                val rule = rules.firstOrNull {
                    it[0] == char && it[1] == charNext
                }
                if (rule != null) {
                    rule[2]
                } else {
                    null
                }

            }
        }

        return polymer
    }

    fun makePolymer(input: List<String>, stepCount: Int): List<Char> {
        val template = input[0].toCharArray().toList()
        val rules = input.asSequence().drop(2).map {
            it.substringBefore(" -> ").toCharArray().toList() to it.substringAfter(" -> ")[0]
        }.toMap()

        var polymer = template
        println("initial")
        println(polymer.joinToString(""))
        var counter = 0
        repeat(stepCount) {
            polymer = step(polymer, rules)
            ++counter
            println("step $counter")
            println(polymer.joinToString(""))
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

        val polymer = makePolymerOptimal(input, 40)

        val freq = mutableMapOf<Char, Long>()

        var pointer: Node<Char>? = polymer
        while(pointer != null) {
            val char = pointer!!.value
            if (freq.containsKey(char)) {
                freq[char] = freq[char]!! + 1L
            } else {
                freq[char] = 1L
            }
            pointer = pointer!!.next
        }
        val max = freq.maxOf { it.value }
        val min = freq.minOf { it.value }

        return max - min
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    val testPart1 = part1(testInput)
    check(testPart1 == 1588) { "test part1 = $testPart1 " }

    //val input = readInput("Day14")
    //val part1 = part1(input)
    //check(part1 == 2851) { "part1 = $part1 " }
    //println(part1)

    //val testPart2 = part2(testInput)
    //check(testPart2 == 2188189693529L) { "test part2 = $testPart2 " }

    //println(part2(input))
}
