package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable

@Serializable
data class WorkflowGraphResp(
    val id: Int,
    val edges: List<WorkflowEdgeEmb>
)