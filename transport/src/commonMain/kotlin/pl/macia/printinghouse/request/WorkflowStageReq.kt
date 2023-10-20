package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
data class WorkflowStageReq(
    val managers: List<Int>,
    val name: String
)