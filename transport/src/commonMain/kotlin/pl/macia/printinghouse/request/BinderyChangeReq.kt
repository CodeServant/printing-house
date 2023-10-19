package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
data class BinderyChangeReq(
    val name: String
)