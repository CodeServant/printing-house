package pl.macia.printinghouse.server.bmodel

import java.time.LocalDateTime
import pl.macia.printinghouse.server.dto.WorkflowStageStop as PWorkflowStageStop

internal class WorkflowStageStopImpl(p: PWorkflowStageStop) : WorkflowStageStop, BusinessBase<PWorkflowStageStop>(p) {
    //TODO constructor
    override var wfssId: Int? by persistent::id
    override var comment: String? by persistent::comment
    override var lastWorkflowStage: Boolean by persistent::lastWorkflowStage
    override var assignTime: LocalDateTime? by persistent::assignTime
    override var createTime: LocalDateTime by persistent::createTime
    override var worker: Worker? by delegate(persistent.worker, ::WorkerImpl, Worker::class.java)
    override var workflowStage: WorkflowStage by delegate(
        persistent.workflowStage,
        ::WorkflowStageImpl,
        WorkflowStage::class.java
    )
    override var order: Order
        get() = TODO("Not yet implemented")
        set(value) {}
}

internal fun toBizWorkflowStageStop(wss: MutableList<PWorkflowStageStop>): BMutableList<WorkflowStageStop, PWorkflowStageStop> {
    return BMutableList(::WorkflowStageStopImpl, {
        it as WorkflowStageStopImpl
        it.persistent
    }, wss)
}