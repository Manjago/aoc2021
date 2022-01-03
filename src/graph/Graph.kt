package graph

interface Graph<V> {
    data class VertexWithEdgeWeight<V>(val vertex: V, val weight: Int)
    fun addEdge(from: V, to: V, weight: Int = 0)
    fun neighbours(vertex: V) : Sequence<V>
    fun neighboursWithEdgeWeight(vertex: V) : Sequence<VertexWithEdgeWeight<V>>
}