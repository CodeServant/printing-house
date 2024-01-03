package pl.macia.printinghouse.request

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class WorkflowStageStopReq(
    val comment: String?,
    val assignTime: LocalDateTime?,
    val createTime: LocalDateTime,
    val workerId: Int?,
    val graphEdgeId: Int
)