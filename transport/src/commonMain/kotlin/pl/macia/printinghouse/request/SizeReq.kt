package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
data class SizeReq(
    val name: String?,
    val heigth: Double,
    val width: Double
)