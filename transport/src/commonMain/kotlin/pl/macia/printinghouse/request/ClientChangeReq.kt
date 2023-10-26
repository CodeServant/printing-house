package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
sealed class ClientChangeReq() {
    abstract val nullingRest: Boolean
    abstract val phoneNumber: String?
    abstract val email: String?
}