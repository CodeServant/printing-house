package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
data class WorkerChangeReq(
    val nullingRest: Boolean = false,
    val isManagerOf: List<Int>? = null,
    val roles: List<Int>? = null,
    val employed: Boolean? = null,
    val activeAccount: Boolean? = null,
    val password: String? = null,
    val email: String? = null,
    val psudoPESEL: String? = null,
    val surname: String? = null,
    val name: String? = null
)