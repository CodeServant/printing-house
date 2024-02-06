package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable

@Serializable
data class BasicLoginResp(
    val authenticated: Boolean,
    val roles: List<String>,
)