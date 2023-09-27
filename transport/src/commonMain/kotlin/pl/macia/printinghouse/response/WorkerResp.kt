package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable

@Serializable
data class WorkerResp(
    val isManagerOf: List<WorkflowStageRespEmb>,
    val roles: List<RoleResp>,
    val employed: Boolean,
    val activeAccount: Boolean,
    val email: String,
    val psudoPESEL: String,
    val surname: String,
    val name: String
)