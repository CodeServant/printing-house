package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*
import jakarta.validation.constraints.PositiveOrZero
import java.math.BigDecimal

const val tableCalculationCard = "CalculationCard"
const val calculationCardOrderId = "orderId"
const val calculationCardBindingCost = "bindingCost"
const val calculationCardEnobling = "enobling"
const val calculationCardOtherCosts = "otherCosts"
const val calculationCardTransport = "transport"
const val calculationCardOrderField = "order"

@Poko
@Entity
@Table(name = tableCalculationCard)
class CalculationCard(
    @Id
    var id: Int?,
    @Column(name = calculationCardBindingCost)
    @field:PositiveOrZero
    var bindingCost: BigDecimal,
    @Column(name = calculationCardEnobling)
    @field:PositiveOrZero
    var enobling: BigDecimal,
    @Column(name = calculationCardOtherCosts)
    @field:PositiveOrZero
    var otherCosts: BigDecimal,
    @Column(name = calculationCardTransport)
    @field:PositiveOrZero
    var transport: BigDecimal,
    @MapsId
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = calculationCardOrderId)
    var order: Order
) {
    constructor(
        bindingCost: BigDecimal,
        enobling: BigDecimal,
        otherCosts: BigDecimal,
        transport: BigDecimal,
        order: Order
    ) : this(null, bindingCost, enobling, otherCosts, transport, order)
}