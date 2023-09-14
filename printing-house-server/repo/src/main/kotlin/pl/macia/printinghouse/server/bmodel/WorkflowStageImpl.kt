package pl.macia.printinghouse.server.bmodel

import pl.macia.printinghouse.server.dto.WorkflowStage as PWorkflowStage
import pl.macia.printinghouse.server.dto.Worker as PWorker

internal class WorkflowStageImpl(
    persistent: PWorkflowStage,
) : WorkflowStage, BusinessBase<PWorkflowStage>(persistent) {
    constructor(
        role: RoleImpl,
        name: String
    ) : this(
        PWorkflowStage(role.persistent, name, mutableListOf())
    )
    override var workflowStageid: Int? by persistent::id
    override var name: String by persistent::name
    override val workflowManagers: BMutableList<Worker, PWorker> =
        BMutableList(::WorkerImpl, {
            it as WorkerImpl
            it.persistent
        }, persistent.workflowStageManagers)
    override var role: Role by delegate(persistent.role, ::RoleImpl, Role::class.java)
}

fun WorkflowStage(role: Role, name: String): WorkflowStage {
    return WorkflowStageImpl(role as RoleImpl, name)
}