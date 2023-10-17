package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
data class SalesmanChangeReq(
    val nullingRest: Boolean = false,
    val employed: Boolean? = null,
    val activeAccount: Boolean? = null,
    val password: String? = null,
    val email: String? = null,
    val psudoPESEL: String? = null,
    val surname: String? = null,
    val name: String? = null
)