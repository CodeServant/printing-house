package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
data class SalesmanChangeReq(
    override val nullingRest: Boolean = false,
    override val employed: Boolean? = null,
    override val activeAccount: Boolean? = null,
    override val password: String? = null,
    override val email: String? = null,
    override val psudoPESEL: String? = null,
    override val surname: String? = null,
    override val name: String? = null
) : EmployeeChangeReq