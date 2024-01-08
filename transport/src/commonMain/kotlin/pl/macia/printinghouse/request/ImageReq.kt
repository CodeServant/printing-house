package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
data class ImageReq(
    val url: String,
    val comment: String?
)
