package pl.macia.printinghouse.server.bmodel

import java.math.BigDecimal

sealed interface CalculationCard {
    var calcCardId: Int?
    var transport: BigDecimal
    var otherCosts: BigDecimal
    var enoblingCost: BigDecimal
    var bindingCost: BigDecimal
    var order: Order
    val printCosts: MutableList<PrintCost>

    fun addPrintCost(
        printCost: BigDecimal,
        matrixCost: BigDecimal,
        printer: Printer
    ): PrintCost
}