package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*

@Poko
@Entity
@Table(name = WorkflowDirEdge.NAME)
internal class WorkflowDirEdge(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = WorkflowStageStop.ID)
    var id: Int?,
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = GRAPH_ID)
    var graph: WorkflowDirGraph,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = V1, nullable = false)
    var v1: WorkflowStage,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = V2, nullable = false)
    var v2: WorkflowStage
) {
    companion object {
        const val NAME = "WorkflowDirEdge"
        const val ID = "id"
        const val GRAPH_ID = "graphId"
        const val GRAPH_FIELD = "graph"
        const val V1 = "V1"
        const val V2 = "V2"
    }
}