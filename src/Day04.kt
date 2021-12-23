fun main() {

    val LEN = 5

    class Bingo(private val data: Array<IntArray> = Array(LEN) { IntArray(LEN) },
                private val marks: Array<BooleanArray> = Array(LEN) { BooleanArray(LEN) },
                var unmarkedSum: Int = 0,
                private val rows: IntArray = IntArray(LEN),
                private val cols: IntArray = IntArray(LEN),
    ) {
        fun load(pos: Int, input: List<String>) : Int {
            for (i in pos + 1 .. pos + LEN) {
                val list = input[i].split(" ").asSequence().filter {
                    it.trim().isNotEmpty()
                }.toList()
                check(list.size == LEN) {"bad size ${list.size}"}
                for(j in 0 until LEN) {
                    val value = list[j].trim().toInt()
                    data[i - pos - 1][j] = value
                    unmarkedSum += value
                }
            }
            return pos + LEN + 1
        }

        fun isWinner() : Boolean {

            for(i in 0 until LEN) {
                if (rows[i] == LEN) {
                    return true
                }
                if (cols[i] == LEN) {
                    return true
                }
            }

            return false
        }

        fun turn(number: Int) {
            for (i in 0 until LEN) {
                for (j in 0 until LEN) {
                    if (data[i][j] == number) {
                        marks[i][j] = true
                        unmarkedSum -= number
                        ++rows[i]
                        ++cols[j]
                        return
                    }
                }
            }
        }

        override fun toString(): String {
            val sb = StringBuilder()
            for(i in 0 until LEN) {
                for(j in 0 until LEN) {
                  if (marks[i][j]) {
                      sb.append("!")
                  }
                  sb.append(data[i][j])
                  sb.append(' ')
                }
                sb.append('\n')
            }
            return sb.toString()
        }

    }

    fun part1(input: List<String>): Int {

        val randomNumbers = input[0].split(",").asSequence().map { it.toInt() }.toList()

        val bingos = mutableListOf<Bingo>()

        var index = 1
        while(index < input.size) {
            val bingo = Bingo()
            index = bingo.load(index, input)
            bingos.add(bingo)
        }

        var winner: Bingo? = null
        var randomIndex = 0
        var justCalledNumber = 0
        while(winner == null) {
            justCalledNumber = randomNumbers[randomIndex++]
            bingos.forEach { it.turn(justCalledNumber) }
            winner = bingos.firstOrNull { it.isWinner() }
        }

        return winner.unmarkedSum * justCalledNumber
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    // 12924 bad

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
