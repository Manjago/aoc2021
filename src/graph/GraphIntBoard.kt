package graph

import IntBoard
import Point

class GraphIntBoard(private val board: IntBoard) : Graph<Point> {

    override fun addEdge(from: Point, to: Point, weight: Int) {
        throw NotImplementedError("Not implemented")
    }

    override fun neighbours(vertex: Point): Sequence<Point> = board.neighbours(vertex)

    override fun neighboursWithEdgeWeight(vertex: Point): Sequence<Graph.VertexWithEdgeWeight<Point>> {
        return board.neighbours(vertex).map {
            Graph.VertexWithEdgeWeight(it, board[it])
        }
    }

}