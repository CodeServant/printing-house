package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
data class ImpositionTypeReq(
    val name: String
)