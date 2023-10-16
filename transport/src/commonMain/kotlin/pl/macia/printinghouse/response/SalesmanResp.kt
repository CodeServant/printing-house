package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable

@Serializable
class SalesmanResp(
    val id: Int,
    val employed: Boolean,
    val activeAccount: Boolean,
    val roles: List<RoleResp>,
    val email: String,
    val psudoPESEL: String,
    val surname: String,
    val name: String
)