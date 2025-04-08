package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable

@Serializable
data class SizeResp(
    val id: Int?,
    val name: String?,
    val heigth: Double,
    val width: Double
)
