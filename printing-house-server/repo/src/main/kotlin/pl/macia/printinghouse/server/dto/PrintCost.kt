package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.PositiveOrZero
import java.math.BigDecimal

@Poko
@Entity
@Table(name = PrintCost.tablePrintCost)
class PrintCost(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = printCostid)
    var id: Int?,
    @ManyToOne(optional = false)
    @JoinColumn(name = printCostorderId, referencedColumnName = CalculationCard.calculationCardOrderId)
    var calculationCard: CalculationCard,
    @ManyToOne(optional = false)
    @JoinColumn(name = printCostprinter, referencedColumnName = Printer.printerId)
    var printer: Printer,
    @Column(name = printCostprintCost)
    @field:PositiveOrZero
    var printCost: BigDecimal,
    @Column(name = printCostmatrixCost)
    @field:PositiveOrZero
    var matrixCost: BigDecimal
) {
    companion object {
        const val tablePrintCost = "PrintCost"
        const val printCostid = "id"
        const val printCostorderId = "orderId"
        const val printCostprinter = "printer"
        const val printCostprintCost = "printCost"
        const val printCostmatrixCost = "matrixCost"
        const val printCostCalcField = "calculationCard"
    }

    constructor(
        calculationCard: CalculationCard,
        printer: Printer,
        printCost: BigDecimal,
        matrixCost: BigDecimal
    ) : this(null, calculationCard, printer, printCost, matrixCost)
}