/*

2: 1
3: 7
4: 4
5: 2, 3, 5
6: 0, 6, 9
7: 8

 */
fun main() {

    data class SevenDigit(val segments: Set<Char>) {
        operator fun plus(other: SevenDigit): SevenDigit = SevenDigit(this.segments.intersect(other.segments))
        fun size() = segments.size
    }

    val text = "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf"
    val digits = text.substringBefore(" | ").split(" ").map {
        SevenDigit(it.toCharArray().toSet())
    }
    println(digits)
    check(SevenDigit("ab".toCharArray().toSet()) == SevenDigit("ba".toCharArray().toSet()))

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
    dict[5] = digits.asSequence().filter { it.size() == 5 && it != dict[3]}
        .first {
            (dict[6]!! + it) == it
        }
    dict[2] = digits.asSequence().filter { it.size() == 5 && it != dict[3] && it != dict[5] }
        .first()

    val dictMap = mutableMapOf<SevenDigit, Int>()
    for(i in dict.indices) {
         dictMap[dict[i]!!] = i
    }

    println(dictMap[SevenDigit("cdfeb".toCharArray().toSet())])
}