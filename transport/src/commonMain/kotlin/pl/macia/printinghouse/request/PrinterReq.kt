package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
data class PrinterReq(
    val name: String,
    val digest: String
)
