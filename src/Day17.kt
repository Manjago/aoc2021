
fun main() {

    data class Velocity(val x: Int, val y: Int)
    data class Position(val x: Int, val y: Int)
    data class Location4D(val position: Position, val velocity: Velocity)
    data class Target(val xRange: IntRange, val yRange: IntRange) {
        fun contains(position: Position) : Boolean = position.x in xRange && position.y in yRange
        fun bye(position: Position) : Boolean = position.y < yRange.first
    }
    data class History(private val data: MutableList<Position> = mutableListOf()) {
        fun addRecord(position: Position) = data.add(position)
        fun maxY() = data.maxOf { it.y }
    }

    fun step(from: Location4D): Location4D = Location4D(
        Position(from.position.x + from.velocity.x, from.position.y + from.velocity.y),
        Velocity(
            when {
                from.velocity.x > 0 -> from.velocity.x - 1
                from.velocity.x < 0 -> from.velocity.x + 1
                else -> 0
            }, from.velocity.y - 1
        )
    )

    fun probe(testedLocation: Location4D, target: Target) : Int? {
        var targetOk = false
        val history = History()
        var location =  testedLocation
        history.addRecord(location.position)
        while(!target.bye(location.position)) {
            location = step(location)
            history.addRecord(location.position)
            if (target.contains(location.position)) {
                targetOk = true
            }
        }
        return if (targetOk) history.maxY() else null
    }


    fun part1(): Int {
        val target = Target(209..238, -86..-59)

        val position = Position(0, 0)

        var maxY = -1
        for(x in 0..238) {
            for(y in 0..300) {
                val foundedVelocity = Velocity(x, y)
                val probe = probe(Location4D(position, foundedVelocity), target)
                if (probe != null && probe > maxY) {
                    maxY = probe
                }
            }
        }
        return maxY ?: error("not found")
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val target = Target(20..30, -10..-5)

    val position = Position(0, 0)
    val velocity = Velocity(6, 9)
    val location =  Location4D(position, velocity)
    println(probe(location, target))

    var maxY = -1
    for(x in 0..30) {
        for(y in 0..30) {
            val foundedVelocity = Velocity(x, y)
            val probe = probe(Location4D(position, foundedVelocity), target)
            if (probe != null && probe > maxY) {
                maxY = probe
           }
        }
    }
    println(maxY)
    println(part1())
}

