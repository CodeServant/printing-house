package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable

@Serializable
data class PaperTypeResp(
    val id: Int,
    val name: String,
    val shortName: String,
)