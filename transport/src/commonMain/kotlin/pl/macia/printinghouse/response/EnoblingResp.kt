package pl.macia.printinghouse.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class EnoblingResp {
    abstract val id: Int
    abstract val name: String
    abstract val description: String?
}

@Serializable
@SerialName("Enobling")
data class GeneralEnoblingResp(
    override val id: Int,
    override val name: String,
    override val description: String?
) : EnoblingResp()

@Serializable
@SerialName("Punch")
data class PunchResp(
    override val id: Int,
    override val name: String,
    override val description: String?
) : EnoblingResp()

@Serializable
@SerialName("UVVarnish")
data class UVVarnishResp(
    override val id: Int,
    override val name: String,
    override val description: String?
) : EnoblingResp()