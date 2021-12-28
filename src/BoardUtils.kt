enum class Direction(val x: Int, val y: Int) {
    NORTH(0, -1), SOUTH(0, 1), WEST(-1, 0), EAST(1, 0)
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

    fun neighbours(point: Point): Sequence<Point> {
        val list = mutableListOf<Point>()
        for (direction in Direction.values()) {
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

}