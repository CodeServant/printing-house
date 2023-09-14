package pl.macia.printinghouse.server.bmodel

import pl.macia.printinghouse.server.dto.Worker as PWorker
import pl.macia.printinghouse.server.dto.Role as PRole
import pl.macia.printinghouse.server.dto.WorkflowStage as PWorkflowStage

internal class WorkerImpl(
    persistent: PWorker,
    override val isManagerOf: BMutableList<WorkflowStage, PWorkflowStage>,
    override val roles: BMutableSet<Role, PRole>
) : Worker, BusinessBase<PWorker>(persistent) {
    override var employed: Boolean by persistent::employed
    override var activeAccount: Boolean by persistent::activeAccount
    override var password: String by persistent::password
    override var email: Email by delegate(persistent.email, ::EmailImpl, Email::class.java)
    override var personId: Int? by persistent::id
    override var psudoPESEL: String by persistent::pseudoPESEL
    override var surname: String by persistent::surname
    override var name: String by persistent::name
}