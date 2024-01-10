package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable

@Serializable
data class PrintCostResp(
    val printCostId: Int,
    val printCost: String,
    val matrixCost: String,
    val printer: PrinterResp
)
