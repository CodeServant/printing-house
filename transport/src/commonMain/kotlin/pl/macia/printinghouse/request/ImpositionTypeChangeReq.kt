package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
data class ImpositionTypeChangeReq(
    val name: String? = null
)
