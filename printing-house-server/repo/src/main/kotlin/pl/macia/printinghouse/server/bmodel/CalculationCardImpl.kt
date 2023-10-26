package pl.macia.printinghouse.server.bmodel

import java.math.BigDecimal
import pl.macia.printinghouse.server.dto.CalculationCard as PCalculationCard

internal class CalculationCardImpl(p: PCalculationCard) : CalculationCard, BusinessBase<PCalculationCard>(p) {
    constructor(
        transport: BigDecimal,
        otherCosts: BigDecimal,
        enoblingCost: BigDecimal,
        bindingCost: BigDecimal,
        order: OrderImpl
    ) : this(
        PCalculationCard(
            transport,
            otherCosts,
            enoblingCost,
            bindingCost,
            order.persistent
        )
    )

    override var calcCardId: Int? by persistent::id
    override var transport: BigDecimal by persistent::transport
    override var otherCosts: BigDecimal by persistent::otherCosts
    override var enoblingCost: BigDecimal by persistent::enobling
    override var bindingCost: BigDecimal by persistent::bindingCost
    override var order: Order by delegate(persistent::order, ::OrderImpl, Order::class.java)
    override val printCosts: MutableList<PrintCost> = toBizCalcCardList(persistent.printCosts)
    override fun addPrintCost(printCost: BigDecimal, matrixCost: BigDecimal, printer: Printer): PrintCost {
        val new = PrintCostImpl(
            printCost,
            matrixCost,
            printer as PrinterImpl,
            this
        )
        printCosts.add(new)
        return new
    }
}