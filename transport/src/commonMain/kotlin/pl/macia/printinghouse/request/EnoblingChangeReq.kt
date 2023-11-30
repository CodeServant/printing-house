package pl.macia.printinghouse.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface IEnoblingChangeReq {
    val nullingRest: Boolean
    val name: String?
    val description: String?
}

@Serializable
@SerialName("Enobling")
data class EnoblingChangeReq(
    override val name: String? = null,
    override val description: String? = null,
    override val nullingRest: Boolean = false
) : IEnoblingChangeReq

@Serializable
@SerialName("UVVarnish")
data class UVVarnishChangeReq(
    override val name: String? = null,
    override val description: String? = null,
    override val nullingRest: Boolean = false
) : IEnoblingChangeReq

@Serializable
@SerialName("Punch")
data class PunchChangeReq(
    override val name: String? = null,
    override val description: String? = null,
    override val nullingRest: Boolean = false
) : IEnoblingChangeReq