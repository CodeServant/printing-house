package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable

@Serializable
data class ColouringResp (
    val colouringId: Byte,
    val secondSide: Byte,
    val firstSide: Byte
)
