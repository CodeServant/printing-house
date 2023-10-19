package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
data class BinderyReq(
    val name: String
)