data class Point(val x: Int, val y: Int)
data class IntBoard(val width: Int, val height: Int) {
    private val data = Array(height) { IntArray(width) }

    fun setValue(x: Int, y: Int, value: Int) {
        data[y][x] = value
    }

    fun getValue(x: Int, y: Int): Int = data[y][x]

    operator fun get(point: Point) : Int = getValue(point.x, point.y)

    fun neighbours(x: Int, y: Int): Sequence<Point> {
        val list = mutableListOf<Point>()
        if (bounded(x + 1, y)) list.add(Point(x + 1, y))
        if (bounded(x - 1, y)) list.add(Point(x - 1, y))
        if (bounded(x, y + 1)) list.add(Point(x, y + 1))
        if (bounded(x, y - 1)) list.add(Point(x, y - 1))
        return list.asSequence()
    }

    private fun bounded(x: Int, y: Int) = x in 0 until width && y in 0 until height

}