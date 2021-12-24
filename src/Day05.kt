import java.lang.Integer.max
import java.lang.Integer.min

fun main() {

    data class Line(
        val x1: Int,
        val y1: Int,
        val x2: Int,
        val y2: Int
    ) {
        fun isVertical() = x1 == x2
        fun isHorizontal() = y1 == y2
    }

    fun stringsToLines(input: List<String>): List<Line> = input.asSequence().map {
        val list4 = it.split("->").asSequence().flatMap { it.split(",") }.map { it.trim().toInt() }.toList()
        Line(list4[0], list4[1], list4[2], list4[3])
    }.filter { it.isVertical() || it.isHorizontal() }
        .toList()

    class Matrix(
        val dimX: Int, val dimY: Int,
        private val data: Array<IntArray> = Array(dimY) { IntArray(dimX) }
    ) {
        fun drawLine(line: Line) {

            when {
                line.isVertical() -> {
                    val startY = min(line.y1, line.y2)
                    val endY = max(line.y1, line.y2)
                    for (i in startY..endY) {
                        ++data[i][line.x1]
                    }
                }
                line.isHorizontal() -> {
                    val startX = min(line.x1, line.x2)
                    val endX = max(line.x1, line.x2)
                    for (i in startX..endX) {
                        ++data[line.y1][i]
                    }
                }
                else -> error("unexpected line $line")
            }
        }

        fun countGreaterOrEqualThen(cellValue: Int): Int {
            var counter = 0
            for (x in 0 until dimX) {
                for (y in 0 until dimY) {
                    if (data[y][x] >= cellValue) {
                        ++counter
                    }
                }
            }
            return counter
        }
    }

    fun part1(input: List<String>): Int {

        val rawLines = stringsToLines(input)

        val maxX = rawLines.maxOf { max(it.x1, it.x2) }
        val maxY = rawLines.maxOf { max(it.y1, it.y2) }
        val minX = rawLines.minOf { min(it.x1, it.x2) }
        val minY = rawLines.minOf { min(it.x1, it.x2) }
        val shiftX = minX
        val shiftY = minY
        val dimX = maxX - shiftX + 1
        val dimY = maxY - shiftY + 1

        val lines = rawLines.asSequence().map {
            Line(
                it.x1 - shiftX, it.y1 - shiftY,
                it.x2 - shiftX, it.y2 - shiftY
            )
        }.toList()

        val matrix = Matrix(dimX, dimY)
        lines.forEach { matrix.drawLine(it) }

        return matrix.countGreaterOrEqualThen(2)
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
