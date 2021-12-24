import java.lang.Integer.max
import java.lang.Integer.signum
import kotlin.math.abs

fun main() {

    data class Line(
        val x1: Int,
        val y1: Int,
        val x2: Int,
        val y2: Int
    ) {
        fun isVertical() = x1 == x2
        fun isHorizontal() = y1 == y2
        fun is45() = abs(x1 - x2) == abs(y1 - y2)
        fun distance() = max(abs(x1 - x2), abs(y1 - y2)) + 1
    }

    fun stringsToLines(input: List<String>): List<Line> = input.asSequence().map {
        val list4 = it.split("->").asSequence().flatMap { it.split(",") }.map { it.trim().toInt() }.toList()
        Line(list4[0], list4[1], list4[2], list4[3])
    }.toList()

    class Matrix(
        val dimX: Int, val dimY: Int,
        private val data: Array<IntArray> = Array(dimY) { IntArray(dimX) }
    ) {
        fun drawLine(line: Line) {
            val stepX = signum(line.x2 - line.x1)
            val stepY = signum(line.y2 - line.y1)

            var x = line.x1
            var y = line.y1
            repeat(line.distance()) {
                ++data[y][x]
                x += stepX
                y += stepY
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

    fun corePart(lines: List<Line>): Int {
        val maxX = lines.maxOf { max(it.x1, it.x2) }
        val maxY = lines.maxOf { max(it.y1, it.y2) }
        val matrix = Matrix(maxX + 1, maxY + 1)
        lines.forEach { matrix.drawLine(it) }

        return matrix.countGreaterOrEqualThen(2)
    }

    fun part1(input: List<String>): Int {
        return corePart(stringsToLines(input).filter { it.isVertical() || it.isHorizontal() })
    }

    fun part2(input: List<String>): Int {
        return corePart(stringsToLines(input).filter { it.isVertical() || it.isHorizontal() || it.is45() })
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    val part1 = part1(testInput)
    check(part1 == 5) { "part1 test = $part1" }
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    check(part1(input) == 5373)
    println(part2(input)) // 21481 too low
}
