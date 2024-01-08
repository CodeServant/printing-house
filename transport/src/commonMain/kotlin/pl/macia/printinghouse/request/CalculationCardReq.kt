package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
data class CalculationCardReq(
    val transport: String,
    val otherCosts: String,
    val enoblingCost: String,
    val bindingCost: String,
    val printCosts: List<PrintCostReq>
)