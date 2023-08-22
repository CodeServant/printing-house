package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.PositiveOrZero
import java.math.BigDecimal

const val tablePrintCost = "PrintCost"
const val printCostid = "id"
const val printCostorderId = "orderId"
const val printCostprinter = "printer"
const val printCostprintCost = "printCost"
const val printCostmatrixCost = "matrixCost"
const val printCostCalcField = "calculationCard"

@Poko
@Entity
@Table(name = tablePrintCost)
class PrintCost(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = printCostid)
    var id: Int?,
    @ManyToOne(cascade = [CascadeType.ALL], optional = false)
    @JoinColumn(name = printCostorderId, referencedColumnName = calculationCardOrderId)
    var calculationCard: CalculationCard,
    @ManyToOne(optional = false)
    @JoinColumn(name = printCostprinter, referencedColumnName = printerId)
    var printer: Printer,
    @Column(name = printCostprintCost)
    @field:PositiveOrZero
    var printCost: BigDecimal,
    @Column(name = printCostmatrixCost)
    @field:PositiveOrZero
    var matrixCost: BigDecimal
) {
    constructor(
        calculationCard: CalculationCard,
        printer: Printer,
        printCost: BigDecimal,
        matrixCost: BigDecimal
    ) : this(null, calculationCard, printer, printCost, matrixCost)
}