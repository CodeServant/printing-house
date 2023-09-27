package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable

@Serializable
data class RoleResp(
    val id: Int,
    val name: String
)