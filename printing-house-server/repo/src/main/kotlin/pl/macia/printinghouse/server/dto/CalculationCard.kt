package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size
import java.math.BigDecimal

@Poko
@Entity
@Table(name = CalculationCard.TABLE_NAME)
internal class CalculationCard(
    @Id
    var id: Int?,
    @Column(name = BINDING_COST)
    @field:PositiveOrZero
    var bindingCost: BigDecimal,
    @Column(name = ENOBLING)
    @field:PositiveOrZero
    var enobling: BigDecimal,
    @Column(name = OTHER_COSTS)
    @field:PositiveOrZero
    var otherCosts: BigDecimal,
    @Column(name = TRANSPORT)
    @field:PositiveOrZero
    var transport: BigDecimal,
    @MapsId
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = ORDER_ID)
    var order: Order,
    @OneToMany(
        mappedBy = PrintCost.CALC_FIELD,
        orphanRemoval = true,
        cascade = [CascadeType.ALL],
        fetch = FetchType.EAGER
    )
    @field:Size(min = 1)
    var printCosts: MutableList<PrintCost> = mutableListOf()
) {
    constructor(
        bindingCost: BigDecimal,
        enobling: BigDecimal,
        otherCosts: BigDecimal,
        transport: BigDecimal,
        order: Order
    ) : this(null, bindingCost, enobling, otherCosts, transport, order)

    fun addPrintCost(
        printer: Printer,
        printCost: BigDecimal,
        matrixCost: BigDecimal
    ): PrintCost {
        val newCost = PrintCost(this, printer, printCost, matrixCost)
        printCosts.add(newCost)
        return newCost
    }

    companion object {
        const val TABLE_NAME = "CalculationCard"
        const val ORDER_ID = "orderId"
        const val BINDING_COST = "bindingCost"
        const val ENOBLING = "enobling"
        const val OTHER_COSTS = "otherCosts"
        const val TRANSPORT = "transport"
        const val ORDER_FIELD = "order"
    }
}