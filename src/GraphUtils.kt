interface Graph<V> {
    fun addEdge(from: V, to: V)
    fun neighbours(vertex: V) : Sequence<V>
}

data class GraphSimple(private val data: MutableMap<String, MutableSet<String>> = mutableMapOf()) : Graph<String> {

    override fun addEdge(from: String, to: String) {
        if (!data.containsKey(from)) {
            data[from] = mutableSetOf()
        }
        data[from]!!.add(to)
    }

    override fun neighbours(vertex: String): Sequence<String> = if (!data.containsKey(vertex)) {
        sequenceOf()
    } else {
        data[vertex]!!.asSequence()
    }


    companion object {
        fun loadBidirectional(input: List<String>): Graph<String> {
            val graph = GraphSimple()
            input.asSequence().forEach {
                val vertexes = it.split("-")
                graph.addEdge(vertexes[0], vertexes[1])
                graph.addEdge(vertexes[1], vertexes[0])
            }
            return graph
        }
    }

}

data class SavedPath<V>(val value: MutableList<V> = mutableListOf()) {
    fun visit(vertex: V) : SavedPath<V> {
        value += vertex
        return this
    }
    fun xcopy() : SavedPath<V> = SavedPath(value.mutableCopyOf())
}
