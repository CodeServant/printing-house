package pl.macia.printinghouse.server.services

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.macia.printinghouse.converting.ConversionException
import pl.macia.printinghouse.request.WorkflowStageReq
import pl.macia.printinghouse.response.PersonsIdentityResp
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.response.WorkflowStageResp
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.bmodel.Worker
import pl.macia.printinghouse.server.bmodel.WorkflowStage
import pl.macia.printinghouse.server.repository.RoleRepo
import pl.macia.printinghouse.server.repository.WorkerRepo
import pl.macia.printinghouse.server.repository.WorkflowStageRepo

@Service
class WorkflowStageServ {
    @Autowired
    private lateinit var repo: WorkflowStageRepo

    @Autowired
    private lateinit var managerRepo: WorkerRepo

    @Autowired
    private lateinit var roleRepo: RoleRepo
    fun listWorkflowStages(): List<WorkflowStageResp>? {
        return repo.findAll().map { it.toTransport() }
    }

    fun findById(id: Int): WorkflowStageResp? {
        return repo.findById(id)?.toTransport()
    }

    @Transactional
    fun insertNew(workflowStageReq: WorkflowStageReq): RecID {
        var workflowStage = WorkflowStage(
            workflowStageReq.name
        )
        val mgrs = managerRepo.findAllById(workflowStageReq.managers)
        workflowStage.workflowManagers.addAll(mgrs)
        mgrs.forEach {
            it.isManagerOf.add(workflowStage)
            it.roles.add(roleRepo.mergeName(PrimaryRoles.WORKFLOW_STAGE_MANAGER))
        }
        workflowStage = repo.save(workflowStage)
        return RecID(workflowStage.workflowStageid!!.toLong())
    }

    @Transactional
    fun deleteWithId(id: Int): RecID {
        val found = repo.findById(id) ?: return RecID(id.toLong())
        found.workflowManagers.forEach {
            it.isManagerOf.remove(found)
            if (it.hasNoWorkflowDuty()) {
                it.removeRole(PrimaryRoles.WORKFLOW_STAGE_MANAGER)
            }
        }
        repo.deleteById(id)
        return RecID(id.toLong())
    }

    private fun Worker.hasNoWorkflowDuty(): Boolean {
        return isManagerOf.isEmpty()
    }

    /**
     * Remove [PrimaryRoles] from worker roles.
     */
    private fun Worker.removeRole(role: String) {
        roles.removeIf {
            it.name == role
        }
    }
}

private fun WorkflowStage.toTransport(): WorkflowStageResp {
    val identities = this.workflowManagers.map {
        PersonsIdentityResp(
            id = it.personId ?: throw ConversionException(),
            name = it.name,
            surname = it.surname
        )
    }
    return WorkflowStageResp(workflowStageid ?: throw ConversionException(), name, identities)
}