package pl.macia.printinghouse.server.services

import jakarta.transaction.Transactional
import kotlinx.datetime.toKotlinLocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.macia.printinghouse.converting.ConversionException
import pl.macia.printinghouse.request.WorkflowGraphChangeReq
import pl.macia.printinghouse.request.WorkflowGraphReq
import pl.macia.printinghouse.response.*
import pl.macia.printinghouse.server.bmodel.WorkflowDirEdge
import pl.macia.printinghouse.server.bmodel.WorkflowDirGraph
import pl.macia.printinghouse.server.bmodel.WorkflowStage
import pl.macia.printinghouse.server.repository.WorkflowDirGraphRepo
import pl.macia.printinghouse.server.repository.WorkflowStageRepo
import java.time.LocalDateTime

@Service
class WorkflowGraphService {
    @Autowired
    private lateinit var repo: WorkflowDirGraphRepo

    @Autowired
    private lateinit var repoWorkflowStage: WorkflowStageRepo
    fun getWholeGraph(id: Int): WorkflowGraphResp? {
        val found = repo.findById(id) ?: return null
        return found.toTransport()
    }

    /**
     * @throws [VertexException] when vertex can't be created.
     */
    @Transactional
    fun insertNewGraph(graphReq: WorkflowGraphReq): RecID {
        val toSave = WorkflowDirGraph(
            creationTime = LocalDateTime.now(),
            name = graphReq.name,
            comment = graphReq.comment
        )

        graphReq.edges.forEach {
            val v1 = repoWorkflowStage.findById(it.v1)
                ?: throw VertexException("there is no ${WorkflowStage::class.simpleName} for ${WorkflowGraphResp::id.name} ${it.v1}")
            val v2 = repoWorkflowStage.findById(it.v2)
                ?: throw VertexException("there is no ${WorkflowStage::class.simpleName} for ${WorkflowGraphResp::id.name} ${it.v2}")
            toSave.addEdge(v1, v2)
        }
        val saved = repo.save(toSave)
        return RecID(saved.wGraphId!!.toLong())
    }

    fun allGraphs(): List<WorkflowGraphResp> {
        return repo.all().map { it.toTransport() }
    }

    @Transactional
    fun changeGraphDetails(id: Int, workflowGraphChangeReq: WorkflowGraphChangeReq): ChangeResp {
        var changed = false
        val graph = repo.findById(id)
            ?: throw NoSuchElementException("no ${WorkflowDirGraph::class.qualifiedName} with the given id")
        val insertName = workflowGraphChangeReq.name
        if (insertName != null && graph.name != insertName) {
            graph.name = insertName
            changed = true
        }
        val insertComment = workflowGraphChangeReq.comment
        if (workflowGraphChangeReq.nullingRest && graph.comment != insertComment) {
            graph.comment = insertComment
            changed = true
        } else if (insertComment != null && graph.comment != insertComment) {
            graph.comment = insertComment
            changed = true
        }
        return ChangeResp(changed)
    }
}

private fun WorkflowStage.checkGraphRequirements() {
    TODO("check if graph properly built")
}

private fun WorkflowDirGraph.toTransport(): WorkflowGraphResp {
    return WorkflowGraphResp(
        id = wGraphId ?: throw ConversionException(),
        edges = edge.map {
            it.toEmbTransport()
        },
        creationTime = creationTime.toKotlinLocalDateTime(),
        changedTime = changedTime?.toKotlinLocalDateTime(),
        name = name,
        comment = comment
    )
}

/**
 * @throws ConversionException
 */
internal fun WorkflowDirEdge.toEmbTransport(): WorkflowEdgeEmb {
    return WorkflowEdgeEmb(
        edgeId = wEdgeId ?: throw ConversionException(),
        v1 = WorkflowStageRespEmb(
            v1.workflowStageid ?: throw ConversionException(),
            v1.name
        ),
        v2 = WorkflowStageRespEmb(
            v2.workflowStageid ?: throw ConversionException(),
            v2.name
        ),
    )
}

