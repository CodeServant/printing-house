package pl.macia.printinghouse.server.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.macia.printinghouse.converting.ConversionException
import pl.macia.printinghouse.response.WorkflowStageResp
import pl.macia.printinghouse.server.bmodel.WorkflowStage
import pl.macia.printinghouse.server.repository.WorkflowStageRepo

@Service
class WorkflowStageServ {
    @Autowired
    lateinit var repo: WorkflowStageRepo
    fun listWorkflowStages(): List<WorkflowStageResp>? {
        return repo.findAll().map { it.toTransport() }
    }

    fun findById(id: Int): WorkflowStageResp? {
        return repo.findById(id)?.toTransport()
    }
}

private fun WorkflowStage.toTransport(): WorkflowStageResp {
    return WorkflowStageResp(workflowStageid ?: throw ConversionException(), name)
}