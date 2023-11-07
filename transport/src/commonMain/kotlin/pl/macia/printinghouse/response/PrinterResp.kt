package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable

@Serializable
data class PrinterResp(
    val id: Int,
    val name: String,
    val digest: String
)