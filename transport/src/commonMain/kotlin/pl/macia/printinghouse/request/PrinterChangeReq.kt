package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
data class PrinterChangeReq(
    val name: String? = null,
    val digest: String? = null
)