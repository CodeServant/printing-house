package pl.macia.printinghouse.server.bmodel

import java.time.LocalDateTime
import pl.macia.printinghouse.server.dto.WorkflowStageStop as PWorkflowStageStop

internal class WorkflowStageStopImpl(p: PWorkflowStageStop) : WorkflowStageStop, BusinessBase<PWorkflowStageStop>(p) {
    constructor(
        comment: String?,
        assignTime: LocalDateTime?,
        createTime: LocalDateTime,
        worker: WorkerImpl?,
        workflowStage: WorkflowStageImpl,
        order: OrderImpl
    ) : this(
        PWorkflowStageStop(
            comment,
            createTime,
            assignTime,
            worker?.persistent,
            workflowStage.persistent,
            order.persistent
        )
    )

    override var wfssId: Int? by persistent::id
    override var comment: String? by persistent::comment
    override var assignTime: LocalDateTime? by persistent::assignTime
    override var createTime: LocalDateTime by persistent::createTime
    override var worker: Worker? by delegate(persistent.worker, ::WorkerImpl, Worker::class.java)
    override var workflowStage: WorkflowStage by delegate(
        persistent.workflowStage,
        ::WorkflowStageImpl,
        WorkflowStage::class.java
    )
    override var order: Order by delegate(persistent.order, ::OrderImpl, Order::class.java)
}

internal fun toBizWorkflowStageStop(wss: MutableList<PWorkflowStageStop>): BMutableList<WorkflowStageStop, PWorkflowStageStop> {
    return BMutableList(::WorkflowStageStopImpl, {
        it as WorkflowStageStopImpl
        it.persistent
    }, wss)
}