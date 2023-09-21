package pl.macia.printinghouse.server.bmodel

import java.math.BigDecimal
import pl.macia.printinghouse.server.dto.PrintCost as PPrintCost

internal class PrintCostImpl(p: PPrintCost) : PrintCost, BusinessBase<PPrintCost>(p) {
    constructor(
        printCost: BigDecimal,
        matrixCost: BigDecimal,
        printer: PrinterImpl,
        calculationCard: CalculationCardImpl
    ) : this(
        PPrintCost(
            printCost = printCost,
            matrixCost = matrixCost,
            printer = printer.persistent,
            calculationCard = calculationCard.persistent
        )
    )

    override var printCostId: Int? by persistent::id
    override var printCost: BigDecimal by persistent::printCost
    override var matrixCost: BigDecimal by persistent::matrixCost
    override var printer: Printer by delegate(persistent.printer, ::PrinterImpl, Printer::class.java)
}

internal fun toBizCalcCardList(printCost: MutableList<PPrintCost>): BMutableList<PrintCost, PPrintCost> {
    return BMutableList(::PrintCostImpl, {
        it as PrintCostImpl
        it.persistent
    }, printCost)
}