package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable

@Serializable
sealed class ClientResp {
    abstract val clientId: Int
    abstract val phoneNumber: String?
    abstract val email: String?
}