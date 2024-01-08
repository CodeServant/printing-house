package pl.macia.printinghouse.server.bmodel

import pl.macia.printinghouse.server.dto.WorkflowDirEdge as PWorkflowDirEdge

internal class WorkflowDirEdgeImpl(p: PWorkflowDirEdge) : WorkflowDirEdge, BusinessBase<PWorkflowDirEdge>(p) {
    override var wEdgeId: Int? by persistent::id
    override var grapf: WorkflowDirGraph by delegate(
        persistent::graph,
        ::WorkflowDirGraphImpl,
        WorkflowDirGraph::class.java
    )
    override var v1: WorkflowStage by delegate(persistent::v1, ::WorkflowStageImpl, WorkflowStage::class.java)
    override var v2: WorkflowStage by delegate(persistent::v2, ::WorkflowStageImpl, WorkflowStage::class.java)
    override fun equals(other: Any?): Boolean {
        if (other is WorkflowDirEdgeImpl)
            return persistent.equals(other.persistent)
        else
            return super.equals(other)
    }

    override fun hashCode(): Int {
        return persistent.hashCode()
    }
}

internal fun toBizWorkflowDirEdge(list: MutableList<PWorkflowDirEdge>): BMutableList<WorkflowDirEdge, PWorkflowDirEdge> {
    return BMutableList(::WorkflowDirEdgeImpl, {
        it as WorkflowDirEdgeImpl
        it.persistent
    }, list)
}