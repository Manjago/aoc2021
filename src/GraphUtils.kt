interface Graph {
    fun addEdge(from: String, to: String)
    fun neighbours(vertex: String) : Sequence<String>
}

data class GraphSimple(private val data: MutableMap<String, MutableSet<String>> = mutableMapOf()) : Graph {

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
        fun loadBidirectional(input: List<String>): Graph {
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

data class SavedPath(val value: MutableList<String> = mutableListOf()) {
    fun visit(vertex: String) : SavedPath {
        value += vertex
        return this
    }
    fun xcopy() : SavedPath = SavedPath(value.mutableCopyOf())
}
