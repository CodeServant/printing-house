package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
data class OrderEnoblingReq(
    val annotation: String?,
    val enoblingId: Int,
    val binderyId: Int,
)