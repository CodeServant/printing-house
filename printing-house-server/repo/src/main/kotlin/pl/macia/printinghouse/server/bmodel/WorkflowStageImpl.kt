package pl.macia.printinghouse.server.bmodel

import pl.macia.printinghouse.server.dto.WorkflowStage as PWorkflowStage
import pl.macia.printinghouse.server.dto.Worker as PWorker

internal class WorkflowStageImpl(
    persistent: PWorkflowStage,
) : WorkflowStage, BusinessBase<PWorkflowStage>(persistent) {
    constructor(
        name: String
    ) : this(
        PWorkflowStage(name, mutableListOf())
    )
    override var workflowStageid: Int? by persistent::id
    override var name: String by persistent::name
    override val workflowManagers: BMutableList<Worker, PWorker> =
        BMutableList(::WorkerImpl, {
            it as WorkerImpl
            it.persistent
        }, persistent.workflowStageManagers)
}

fun WorkflowStage(name: String): WorkflowStage {
    return WorkflowStageImpl(name)
}