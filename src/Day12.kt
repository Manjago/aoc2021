fun main() {

    fun String.isLower() = this.toCharArray().asSequence().all { it.isLowerCase() }

    fun part1(input: List<String>): Int {
        val graph = GraphSimple.loadBidirectional(input)

        val queue = ArrayDeque<SavedPath>()
        val initPath = SavedPath().visit("start")
        queue += initPath
        val visited = mutableSetOf<SavedPath>()

        while(queue.isNotEmpty()) {
            val path = queue.removeFirst()
            if (visited.contains(path)) {
                continue
            } else {
                visited += path
            }

            val visitedSmallCaves = path.value.asSequence().filter { it.isLower() }.toSet()
            graph.neighbours(path.value.last()).filter { !visitedSmallCaves.contains(it) }.forEach {
                queue += path.xcopy().visit(it)
            }
        }

        val refined = visited.asSequence().filter { it.value.last() == "end" }.toList()

        return refined.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput0 = readInput("Day12_test_0")
    val test0part1 = part1(testInput0)
    check(test0part1 == 10) {"test0part1 = $test0part1"}

    val testInput1 = readInput("Day12_test_1")
    val test1part1 = part1(testInput1)
    check(test1part1 == 19) {"test1part1 = $test1part1"}

    val testInput2 = readInput("Day12_test_2")
    val test2part1 = part1(testInput2)
    check(test2part1 == 226) {"test2part1 = $test2part1"}

    val input = readInput("Day12")
    println(part1(input))
    //println(part2(input))
}
