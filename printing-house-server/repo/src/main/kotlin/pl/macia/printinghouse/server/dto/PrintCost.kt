package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.PositiveOrZero
import java.math.BigDecimal

@Poko
@Entity
@Table(name = PrintCost.TABLE_NAME)
internal class PrintCost(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    var id: Int?,
    @ManyToOne(optional = false)
    @JoinColumn(name = ORDER_ID, referencedColumnName = CalculationCard.ORDER_ID)
    var calculationCard: CalculationCard,
    @ManyToOne(optional = false)
    @JoinColumn(name = PRINTER, referencedColumnName = Printer.ID)
    var printer: Printer,
    @Column(name = PRINT_COST)
    @field:PositiveOrZero
    var printCost: BigDecimal,
    @Column(name = MATRIX_COST)
    @field:PositiveOrZero
    var matrixCost: BigDecimal
) {
    companion object {
        const val TABLE_NAME = "PrintCost"
        const val ID = "id"
        const val ORDER_ID = "orderId"
        const val PRINTER = "printer"
        const val PRINT_COST = "printCost"
        const val MATRIX_COST = "matrixCost"
        const val CALC_FIELD = "calculationCard"
    }

    constructor(
        calculationCard: CalculationCard,
        printer: Printer,
        printCost: BigDecimal,
        matrixCost: BigDecimal
    ) : this(null, calculationCard, printer, printCost, matrixCost)
}