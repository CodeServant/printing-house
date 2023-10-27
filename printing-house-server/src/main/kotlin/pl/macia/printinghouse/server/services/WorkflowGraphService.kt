package pl.macia.printinghouse.server.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.macia.printinghouse.converting.ConversionException
import pl.macia.printinghouse.response.WorkflowEdgeEmb
import pl.macia.printinghouse.response.WorkflowGraphResp
import pl.macia.printinghouse.response.WorkflowStageRespEmb
import pl.macia.printinghouse.server.bmodel.WorkflowDirEdge
import pl.macia.printinghouse.server.bmodel.WorkflowDirGraph
import pl.macia.printinghouse.server.repository.WorkflowDirGraphRepo

@Service
class WorkflowGraphService {
    @Autowired
    private lateinit var repo: WorkflowDirGraphRepo
    fun getWholeGraph(id: Int): WorkflowGraphResp? {
        val found = repo.findById(id) ?: return null
        return found.toTransport()
    }
}

private fun WorkflowDirGraph.toTransport(): WorkflowGraphResp {
    return WorkflowGraphResp(
        id = wGraphId ?: throw ConversionException(),
        edges = edge.map {
            it.toEmbTransport()
        }
    )
}

private fun WorkflowDirEdge.toEmbTransport(): WorkflowEdgeEmb {
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

