package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*

@Poko
@Entity
@Table(name = OrderEnobling.TABLE_NAME)
internal class OrderEnobling(
    @Id
    @Column(name = ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = ENOBLING, referencedColumnName = Enobling.ID)
    var enobling: Enobling,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = BINDERY, referencedColumnName = Bindery.ID)
    var bindery: Bindery,
    @Column(name = ANNOTATION)
    var annotation: String?,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = ORDER, nullable = false)
    var order: Order
) {
    companion object {
        const val TABLE_NAME = "OrderEnobling"
        const val ID = "id"
        const val ENOBLING = "enobling"
        const val ORDER = "`order`"
        const val BINDERY = "bindery"
        const val ANNOTATION = "annotation"
        const val ORDER_FIELD = "order"
    }

    constructor(enobling: Enobling, bindery: Bindery, annotation: String?, order: Order) : this(
        null,
        enobling,
        bindery,
        annotation,
        order
    )
}