package pl.macia.printinghouse.server.bmodel

import org.jgrapht.graph.DirectedAcyclicGraph
import java.time.LocalDateTime
import java.util.function.Supplier

interface WorkflowDirGraph {
    var wGraphId: Int?
    var creationTime: LocalDateTime
    var comment: String?
    var name: String
    var changedTime: LocalDateTime?
    val edge: MutableList<WorkflowDirEdge>
    private val vertexes: Set<WorkflowStage>
        get() {
            val set = mutableSetOf<WorkflowStage>()
            //val edges = edge.toList()
            set.addAll(edge.map {
                it.v1
            })
            set.addAll(edge.map {
                it.v2
            })
            return set
        }

    /**
     * Edges, that the source vertices of this edge don't have any incoming edges.
     *
     */
    val startEdges: Set<WorkflowDirEdge>
        get() {
            val graph = DirectedAcyclicGraph<WorkflowStage, WorkflowDirEdge>(null, EdgeSupplier(edge), false)
            vertexes.forEach(graph::addVertex)

            edge.forEach {
                graph.addEdge(it.v1, it.v2)
            }
            return graph.edgeSet().filter {
                val source = graph.getEdgeSource(it)
                graph.inDegreeOf(source) == 0
            }.toSet()
        }

    fun addEdge(v1: WorkflowStage, v2: WorkflowStage): WorkflowDirEdge
}

private class EdgeSupplier(val edges: List<WorkflowDirEdge>) : Supplier<WorkflowDirEdge> {
    var i = 0
    override fun get(): WorkflowDirEdge {
        return edges[i++]
    }
}