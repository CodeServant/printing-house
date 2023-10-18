package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable

@Serializable
data class BinderyResp(
    val id: Int,
    val name: String
)