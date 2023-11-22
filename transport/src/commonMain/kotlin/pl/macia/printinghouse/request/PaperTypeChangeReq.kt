package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
data class PaperTypeChangeReq(
    val name: String? = null,
    val shortName: String? = null,
)
