data class Point(val x: Int, val y: Int)
data class IntBoard(val width: Int, val height: Int) {
    private val data = Array(height) { IntArray(width) }
    fun setValue(x: Int, y: Int, value: Int) {
        data[y][x] = value
    }

    fun safeGetValue(x: Int, y: Int, outboundValue: Int): Int = if (x in 0 until width && y in 0 until height) {
        data[y][x]
    } else {
        outboundValue
    }

    fun getValue(x: Int, y: Int): Int = data[y][x]

}