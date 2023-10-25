package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
sealed class ClientReq {
    abstract val phoneNumber: String?
    abstract val email: String?
}