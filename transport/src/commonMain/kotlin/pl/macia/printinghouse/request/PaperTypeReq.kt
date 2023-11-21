package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
data class PaperTypeReq(
    val name: String,
    val shortName: String,
)