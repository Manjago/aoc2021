package graph

fun loadBidirectional(input: List<String>): Graph<String> {
    val graph = GraphSimple<String>()
    input.asSequence().forEach {
        val vertexes = it.split("-")
        graph.addEdge(vertexes[0], vertexes[1])
        graph.addEdge(vertexes[1], vertexes[0])
    }
    return graph
}
