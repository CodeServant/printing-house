package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.bmodel.WorkflowStage
import pl.macia.printinghouse.server.bmodel.WorkflowStageImpl
import pl.macia.printinghouse.server.dao.WorkflowStageDAO
import pl.macia.printinghouse.server.dto.WorkflowStage as PWorkflowStage

@Repository
internal class WorkflowStageRepoImpl : WorkflowStageIntRepo {
    @Autowired
    lateinit var dao: WorkflowStageDAO
    private fun PWorkflowStage.toBiz(): WorkflowStageImpl = WorkflowStageImpl(this)

    override fun save(obj: WorkflowStage): WorkflowStage {
        obj as WorkflowStageImpl
        return dao.save(obj.persistent).toBiz()
    }

    override fun findById(id: Int): WorkflowStage? {
        return dao.findByIdOrNull(id)?.toBiz()
    }

    override fun findAllById(ids: Iterable<Int>): List<WorkflowStage> {
        return dao.findAllById(ids).map { it.toBiz() }
    }
}