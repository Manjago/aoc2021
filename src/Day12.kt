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

            if (path.value.last() == "end") {
                continue
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

            if (path.value.last() == "end") {
                continue
            }

            // already has two-visited small cave?
            val hasTwoVisitedSmallCave = path.value.filter { it.isLower() }
                .groupingBy { it }.eachCount().filter { it.value > 1 }.isNotEmpty()

            if (hasTwoVisitedSmallCave) {
                val visitedSmallCaves = path.value.asSequence().filter { it.isLower() }.toSet()
                graph.neighbours(path.value.last()).filter { !visitedSmallCaves.contains(it) }.forEach {
                    queue += path.xcopy().visit(it)
                }
            } else {
                graph.neighbours(path.value.last()).filter { it != "start" }.forEach {
                    queue += path.xcopy().visit(it)
                }
            }

        }

        val refined = visited.asSequence().filter { it.value.last() == "end" }.toList()

        return refined.size
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
    val part1 = part1(input)
    check(part1 == 5576) {"part1 = $part1"}
    println(part1)

    val test0part2 = part2(testInput0)
    check(test0part2 == 36) {"test0part2 = $test0part2"}
    val test1part2 = part2(testInput1)
    check(test1part2 == 103) {"test1part2 = $test1part2"}
    val test2part2 = part2(testInput2)
    check(test2part2 == 3509) {"test2part2 = $test2part2"}

    println(part2(input))
}
