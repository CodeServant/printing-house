package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
data class WorkflowEdgeReq(
    val v1: Int,
    val v2: Int
)