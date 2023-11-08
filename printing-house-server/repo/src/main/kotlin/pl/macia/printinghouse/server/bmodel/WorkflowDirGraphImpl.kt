package pl.macia.printinghouse.server.bmodel

import java.time.LocalDateTime
import pl.macia.printinghouse.server.dto.WorkflowDirGraph as PWorkflowDirGraph

internal class WorkflowDirGraphImpl(p: PWorkflowDirGraph) : WorkflowDirGraph, BusinessBase<PWorkflowDirGraph>(p) {
    constructor(
        creationTime: LocalDateTime,
        name: String,
        changedTime: LocalDateTime?,
        comment: String?
    ) : this(
        PWorkflowDirGraph(
            null,
            creationTime,
            comment,
            name,
            changedTime
        )
    )

    override var wGraphId: Int? by persistent::id
    override var creationTime: LocalDateTime by persistent::creationTime
    override var comment: String? by persistent::comment
    override var name: String by persistent::name
    override var changedTime: LocalDateTime? by persistent::changedTime
    override val edge: MutableList<WorkflowDirEdge> = toBizWorkflowDirEdge(persistent.edges)
    override fun addEdge(v1: WorkflowStage, v2: WorkflowStage): WorkflowDirEdge {
        v1 as WorkflowStageImpl
        v2 as WorkflowStageImpl
        return WorkflowDirEdgeImpl(persistent.addEdge(v1.persistent, v2.persistent))
    }
}

fun WorkflowDirGraph(
    creationTime: LocalDateTime,
    name: String,
    changedTime: LocalDateTime? = null,
    comment: String?
): WorkflowDirGraph {
    return WorkflowDirGraphImpl(
        creationTime,
        name,
        changedTime,
        comment
    )
}