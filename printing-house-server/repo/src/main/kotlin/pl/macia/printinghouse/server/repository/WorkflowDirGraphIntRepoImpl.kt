package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.bmodel.WorkflowDirGraph
import pl.macia.printinghouse.server.bmodel.WorkflowDirGraphImpl
import pl.macia.printinghouse.server.dao.WorkflowDirGraphDAO
import pl.macia.printinghouse.server.dto.WorkflowDirGraph as PWorkflowDirGraph

@Repository
internal class WorkflowDirGraphIntRepoImpl : WorkflowDirGraphIntRepo {
    @Autowired
    lateinit var dao: WorkflowDirGraphDAO
    private fun PWorkflowDirGraph.toBiz(): WorkflowDirGraphImpl {
        return WorkflowDirGraphImpl(this)
    }

    override fun save(obj: WorkflowDirGraph): WorkflowDirGraph {
        obj as WorkflowDirGraphImpl
        return dao.save(obj.persistent).toBiz()
    }

    override fun findById(id: Int): WorkflowDirGraph? {
        return dao.findByIdOrNull(id)?.toBiz()
    }
}