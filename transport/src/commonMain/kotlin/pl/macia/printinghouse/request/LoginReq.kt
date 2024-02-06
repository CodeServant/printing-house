package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginReq(
    val login: String,
    val password: String
)