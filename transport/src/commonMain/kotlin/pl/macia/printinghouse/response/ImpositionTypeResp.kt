package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable

@Serializable
data class ImpositionTypeResp(
    val id: Int,
    val name: String
)