import graph.Graph
import graph.GraphIntBoard
import java.util.PriorityQueue
import kotlin.time.ExperimentalTime
import kotlin.time.TimedValue
import kotlin.time.measureTimedValue

@OptIn(ExperimentalTime::class)
fun main() {

    // https://ru.wikipedia.org/wiki/%D0%90%D0%BB%D0%B3%D0%BE%D1%80%D0%B8%D1%82%D0%BC_%D0%94%D0%B5%D0%B9%D0%BA%D1%81%D1%82%D1%80%D1%8B
    fun dijkstraQueue(board: IntBoard): Int {
        val graph = GraphIntBoard(board)
        val labels = board.all().map {
            it to
                    (if (it == Point(0, 0)) 0 else Int.MAX_VALUE)
        }.toMap().toMutableMap()
        val visited = mutableSetOf<Point>()

        val queue = PriorityQueue<Graph.VertexWithEdgeWeight<Point>> { o1, o2 -> o1.weight.compareTo(o2.weight) }
        queue.add(Graph.VertexWithEdgeWeight(Point(0, 0), 0))

        while (queue.isNotEmpty()) {

            val u = queue.remove().vertex
            if (!visited.contains(u)) {
                graph.neighboursWithEdgeWeight(u).filter { vertexAndWeight ->
                    !visited.contains(vertexAndWeight.vertex)
                }.forEach {
                    val newLen = labels[u]!! + it.weight
                    if (newLen < labels[it.vertex]!!) {
                        labels[it.vertex] = newLen
                        queue.add(Graph.VertexWithEdgeWeight(it.vertex, newLen))
                    }
                }
                visited.add(u)
            }
        }

        return labels[Point(board.width - 1, board.height - 1)]!!
    }

    fun part1(input: List<String>): Int {
        val board = IntBoard.loadBoard(input)
        return dijkstraQueue(board)
    }

    fun IntBoard.addRight(otherBoard: IntBoard): IntBoard {
        check(this.height == otherBoard.height) { "heights  must be equal" }

        val result = IntBoard(width + otherBoard.width, height)
        for (y in 0 until height) {
            for (x in 0 until width) {
                val point = Point(x, y)
                result[point] = this[point]
            }
            for (x in width until width + otherBoard.width) {
                result[Point(x, y)] = otherBoard[Point(x - width, y)]
            }
        }
        return result
    }

    fun IntBoard.addBottom(otherBoard: IntBoard): IntBoard {
        check(this.width == otherBoard.width) { "widths  must be equal" }

        val result = IntBoard(width, height + otherBoard.height)
        for (x in 0 until width) {
            for (y in 0 until height) {
                val point = Point(x, y)
                result[point] = this[point]
            }
            for (y in height until height + otherBoard.height) {
                result[Point(x, y)] = otherBoard[Point(x, y - height)]
            }
        }
        return result
    }

    fun IntBoard.incRotate(limit: Int): IntBoard {
        val result = IntBoard(width, height)
        this.all().forEach {
            val newValue = this[it] + 1
            result[it] = if (newValue > limit) 1 else newValue
        }
        return result
    }

    fun part2(input: List<String>): Int {
        var workBoard = IntBoard.loadBoard(input)
        var resultBoard = workBoard.xcopy()

        repeat(4) {
            workBoard = workBoard.incRotate(9)
            resultBoard = resultBoard.addRight(workBoard)
        }

        workBoard = resultBoard.xcopy()
        repeat(4) {
            workBoard = workBoard.incRotate(9)
            resultBoard = resultBoard.addBottom(workBoard)
        }

        return dijkstraQueue(resultBoard)
    }

    val testInput = readInput("Day15_test")
    val testPart1 = part1(testInput)
    check(testPart1 == 40) { "test part1 = $testPart1" }

    val input = readInput("Day15")
    val part1: TimedValue<Int> = measureTimedValue { part1(input) }
    check(part1.value == 462) { "part1 = ${part1.value}" }
    println("part1 answer ${part1.value}, elapsed ${part1.duration}")

    val testPart2 = part2(testInput)
    check(testPart2 == 315) { "test part2 = $testPart2" }

    val part2: TimedValue<Int> = measureTimedValue { part2(input) }
    check(part2.value == 2846) { "part2 = ${part2.value}" }
    println("part2 answer ${part2.value}, elapsed ${part2.duration}")
}
