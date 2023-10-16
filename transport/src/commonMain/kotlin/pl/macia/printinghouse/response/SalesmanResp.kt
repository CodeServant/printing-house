package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable

@Serializable
data class SalesmanResp(
    val id: Int,
    val employed: Boolean,
    val activeAccount: Boolean,
    val roles: List<RoleResp>, // todo hide roles from rest api user
    val email: String,
    val psudoPESEL: String,
    val surname: String,
    val name: String
)