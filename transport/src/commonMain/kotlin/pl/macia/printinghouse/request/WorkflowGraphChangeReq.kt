package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
data class WorkflowGraphChangeReq(
    val nullingRest: Boolean = false,
    val name: String? = null,
    val comment: String? = null
)