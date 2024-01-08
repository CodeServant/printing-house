package pl.macia.printinghouse.response

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class WorkflowStageStopResp(
    val wfssId: Int?,
    val comment: String?,
    val assignTime: LocalDateTime?,
    val createTime: LocalDateTime,
    val worker: WorkerResp?,
    val graphEdge: WorkflowEdgeEmb
)