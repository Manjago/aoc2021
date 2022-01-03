package graph

data class GraphSimple<T>(private val data: MutableMap<T, MutableSet<T>> = mutableMapOf()) : Graph<T> {

    override fun addEdge(from: T, to: T) {
        if (!data.containsKey(from)) {
            data[from] = mutableSetOf()
        }
        data[from]!!.add(to)
    }

    override fun neighbours(vertex: T): Sequence<T> = if (!data.containsKey(vertex)) {
        sequenceOf()
    } else {
        data[vertex]!!.asSequence()
    }

}