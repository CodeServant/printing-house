package pl.macia.printinghouse.server.bmodel

import java.math.BigDecimal

interface CalculationCard {
    var calcCardId: Int?
    var transport: BigDecimal
    var otherCosts: BigDecimal
    var enoblingCost: BigDecimal
    var bindingCost: BigDecimal
    var order: Order
    var printCosts: List<PrintCost>
}