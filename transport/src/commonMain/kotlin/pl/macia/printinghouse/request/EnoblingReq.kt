package pl.macia.printinghouse.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class IEnoblingReq {
    abstract val name: String
    abstract val description: String?
}

@Serializable
@SerialName("Enobling")
data class EnoblingReq(override val name: String, override val description: String?) : IEnoblingReq()

@Serializable
@SerialName("UVVarnish")
data class UVVarnishReq(override val name: String, override val description: String?) : IEnoblingReq()

@Serializable
@SerialName("Punch")
data class PunchReq(override val name: String, override val description: String?) : IEnoblingReq()