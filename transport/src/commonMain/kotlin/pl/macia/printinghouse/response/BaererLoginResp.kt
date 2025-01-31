package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable

@Serializable
data class BaererLoginResp(
    val authenticated: Boolean,
    val token: String,
    val roles: List<String>,
)