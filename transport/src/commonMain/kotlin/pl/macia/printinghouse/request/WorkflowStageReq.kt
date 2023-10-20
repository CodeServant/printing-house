package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
data class WorkflowStageReq(
    val name: String
)