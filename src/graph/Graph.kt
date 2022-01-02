package graph

interface Graph<V> {
    fun addEdge(from: V, to: V)
    fun neighbours(vertex: V) : Sequence<V>
}