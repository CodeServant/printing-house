package pl.macia.printinghouse.server.bmodel

import java.math.BigDecimal

sealed interface PrintCost {
    var printCostId: Int?
    var printCost: BigDecimal
    var matrixCost: BigDecimal
    var printer: Printer
}