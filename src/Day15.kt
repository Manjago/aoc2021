import graph.GraphIntBoard

fun main() {
    fun part1(input: List<String>): Int {

        val board = IntBoard.loadBoard(input)
        val graph = GraphIntBoard(board)
        val labels = board.all().map {
            it to
                    (if (it == Point(0, 0)) 0 else Int.MAX_VALUE)
        }.toMap().toMutableMap()
        val visited = mutableSetOf<Point>()
        val vertexCount = board.height * board.width

        while (visited.size < vertexCount) {
            val u = board.all().filter { !visited.contains(it) }.sortedBy { labels[it] }.first()
            graph.neighboursWithEdgeWeight(u).filter { vertexAndWeight ->
                !visited.contains(vertexAndWeight.vertex)
            }.forEach {
                val newLen = labels[u]!! + it.weight
                if (newLen < labels[it.vertex]!!) {
                    labels[it.vertex] = newLen
                }
            }
            visited.add(u)
        }

        return labels[Point(board.width - 1, board.height - 1)]!!
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day15_test")
    val testPart1 = part1(testInput)
    check(testPart1 == 40) { "test part1 = $testPart1" }

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}
