package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable

/**
 * Is embedded object of some other transport type.
 */
@Serializable
data class WorkflowStageResp(
    val id: Int,
    val name: String
)