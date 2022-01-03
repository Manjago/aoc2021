enum class Direction(val x: Int, val y: Int, val level: Int) {
    NORTH(0, -1, 0), SOUTH(0, 1, 0), WEST(-1, 0, 0), EAST(1, 0, 0),
    NE(1, -1, 1), NW(-1, -1, 1), SE(1, 1, 1), SW(-1, 1, 1),
}

data class Point(val x: Int, val y: Int) {
    fun neighbour(direction: Direction) = Point(this.x + direction.x, this.y + direction.y)
}

data class IntBoard(val width: Int, val height: Int) {
    private val data = Array(height) { IntArray(width) }

    operator fun get(point: Point): Int = data[point.y][point.x]
    operator fun set(point: Point, value: Int) {
        data[point.y][point.x] = value
    }

    fun neighbours(point: Point, levelLimit: Int = 0): Sequence<Point> {
        val list = mutableListOf<Point>()
        for (direction in Direction.values().filter { it.level <=levelLimit }) {
            with(point.neighbour(direction)) {
                if (bounded(this)) list.add(this)
            }
        }
        return list.asSequence()
    }

    fun all(): Sequence<Point> = generateSequence(Point(0, 0)) {
        it.next()
    }

    private fun bounded(point: Point) = point.x in 0 until width && point.y in 0 until height

    private fun Point.next(): Point? = when {
        x == width - 1 && y == height - 1 -> null
        x == width - 1 -> Point(0, y + 1)
        else -> Point(x + 1, y)
    }

    fun display() : String {
        val sb = StringBuilder()
        var current: Point? = Point(0,0)
        var counter = 0;
        while(current != null) {
            sb.append(this[current])
            current = current.next()
            ++counter
            if (counter % width == 0) {
                sb.append('\n')
            }
        }
        sb.append("-".repeat(width))
        return sb.toString()
    }

    fun xcopy() : IntBoard {
        val result = IntBoard(width, height)
        this.all().forEach {
            result[it] =  this[it]
        }
        return result
    }

    companion object {
        fun loadBoard(input: List<String>): IntBoard {
            val board = IntBoard(input[0].length, input.size)

            for (j in input.indices) {
                val row = input[j].toCharArray().asSequence().map { it.toString().toInt() }.toList().toIntArray()
                for (i in row.indices) {
                    board[Point(i, j)] = row[i]
                }
            }
            return board
        }
    }
}