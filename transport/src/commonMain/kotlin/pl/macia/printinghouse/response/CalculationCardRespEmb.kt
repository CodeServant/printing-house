package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable

@Serializable
data class CalculationCardRespEmb (
    val id: Int,
    val transport: String,
    val otherCosts: String,
    val enoblingCost: String,
    val bindingCost: String,
    val printCosts: List<PrintCostResp>
)