package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
data class PrintCostReq(
    val printCost: String,
    val matrixCost: String,
    val printerId: Int
)