package pl.macia.printinghouse.response

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class WorkflowGraphResp(
    val id: Int,
    val edges: List<WorkflowEdgeEmb>,
    val name: String,
    val comment: String?,
    val creationTime: LocalDateTime,
    val changedTime: LocalDateTime?
)