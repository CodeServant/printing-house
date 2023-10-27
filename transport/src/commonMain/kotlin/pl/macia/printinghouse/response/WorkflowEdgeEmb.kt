package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable

@Serializable
data class WorkflowEdgeEmb(
    val edgeId: Int,
    val v1: WorkflowStageRespEmb,
    val v2: WorkflowStageRespEmb
)