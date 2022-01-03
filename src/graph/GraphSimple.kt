package graph

data class GraphSimple<T>(private val data: MutableMap<T, MutableSet<Graph.VertexWithEdgeWeight<T>>> = mutableMapOf()) :
    Graph<T> {

    override fun addEdge(from: T, to: T, weight: Int) {
        if (!data.containsKey(from)) {
            data[from] = mutableSetOf()
        }
        data[from]!!.add(Graph.VertexWithEdgeWeight(to, weight))
    }

    override fun neighbours(vertex: T): Sequence<T> = if (!data.containsKey(vertex)) {
        sequenceOf()
    } else {
        data[vertex]!!.asSequence().map { it.vertex }
    }

    override fun neighboursWithEdgeWeight(vertex: T): Sequence<Graph.VertexWithEdgeWeight<T>> =
        if (!data.containsKey(vertex)) {
            sequenceOf()
        } else {
            data[vertex]!!.asSequence()
        }

}