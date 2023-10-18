package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
data class WorkerChangeReq(
    override val nullingRest: Boolean = false,
    val isManagerOf: List<Int>? = null,
    override val employed: Boolean? = null,
    override val activeAccount: Boolean? = null,
    override val password: String? = null,
    override val email: String? = null,
    override val psudoPESEL: String? = null,
    override val surname: String? = null,
    override val name: String? = null
) : EmployeeChangeReq