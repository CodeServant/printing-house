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

fun WorkflowDirGraph(
    creationTime: LocalDateTime,
    name: String,
    changedTime: LocalDateTime?,
    comment: String?
): WorkflowDirGraph {
    return WorkflowDirGraphImpl(
        creationTime,
        name,
        changedTime,
        comment
    )
}