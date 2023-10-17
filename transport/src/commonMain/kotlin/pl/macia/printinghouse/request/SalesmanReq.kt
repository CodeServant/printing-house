package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
data class SalesmanReq(
    val employed: Boolean,
    val activeAccount: Boolean,
    val password: String,
    val email: String,
    val psudoPESEL: String,
    val surname: String,
    val name: String
)