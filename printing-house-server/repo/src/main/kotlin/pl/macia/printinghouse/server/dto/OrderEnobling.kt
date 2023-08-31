package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*

@Poko
@Entity
@Table(name = OrderEnobling.tableOrderEnobling)
class OrderEnobling internal constructor(
    @Id
    @Column(name = orderEnoblingId)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = orderEnoblingEnobling, referencedColumnName = Enobling.enoblingId)
    var enobling: Enobling,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = orderEnoblingBindery, referencedColumnName = Bindery.binderyId)
    var bindery: Bindery,
    @Column(name = orderEnoblingAnnotation)
    var annotation: String?,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = orderEnoblingOrder, nullable = false)
    val order: Order
) {
    companion object {
        const val tableOrderEnobling = "OrderEnobling"
        const val orderEnoblingId = "id"
        const val orderEnoblingEnobling = "enobling"
        const val orderEnoblingOrder = "`order`"
        const val orderEnoblingBindery = "bindery"
        const val orderEnoblingAnnotation = "annotation"
        const val orderEnoblingOrderField = "order"
    }

    internal constructor(enobling: Enobling, bindery: Bindery, annotation: String?, order: Order) : this(
        null,
        enobling,
        bindery,
        annotation,
        order
    )
}