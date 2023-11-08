package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
data class WorkflowGraphReq(
    val edges:List<WorkflowEdgeReq>,
    var comment: String? = null,
    var name: String,
)