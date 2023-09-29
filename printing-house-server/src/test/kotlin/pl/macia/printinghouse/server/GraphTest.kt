package pl.macia.printinghouse.server

import org.jgrapht.graph.DirectedAcyclicGraph
import org.junit.jupiter.api.Test
import java.util.function.Supplier

class GraphTest {
    @Test
    fun `acyclic graph`() {

        val graph = DirectedAcyclicGraph<Int, Int>(null, edgeSup, true)
        listOf(1, 2, 3, 4, 5).forEach(graph::addVertex)


        graph.addEdge(1, 2)
        graph.addEdge(2, 3)
        graph.addEdge(3, 5)
        graph.addEdge(2, 4)
        graph.addEdge(4, 5)
        graph.setEdgeWeight(2, 3, 10.0)


        graph.incomingEdgesOf(5)
            .maxBy{graph.getEdgeWeight(it)}
            .let { println(graph.getEdgeWeight(it)) }
    }
}

private val edgeSup: Supplier<Int> = object : Supplier<Int> {
    var i = 0
    override fun get(): Int {
        return i++
    }

}