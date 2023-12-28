package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable

@Serializable
data class OrderEnoblingResp(
    val id: Int,
    val annotation: String?,
    val enobling: EnoblingResp,
    val bindery: BinderyResp
)
