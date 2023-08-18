package pl.macia.printinghouse.server.dto

import dev.drewhamilton.poko.Poko
import jakarta.persistence.*

const val tableOrderEnobling = "OrderEnobling"
const val orderEnoblingId = "id"
const val orderEnoblingEnobling = "enobling"
const val orderEnoblingOrder = "order"
const val orderEnoblingBindery = "bindery"
const val orderEnoblingAnnotation = "annotation"

@Poko
@Entity
@Table(name = tableOrderEnobling)
class OrderEnobling(
    @Id
    @Column(name = orderEnoblingId)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?,
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = orderEnoblingEnobling, referencedColumnName = enoblingId)
    var enobling: Enobling,
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = orderEnoblingBindery, referencedColumnName = binderyId)
    var bindery: Bindery,
    @Column(name = orderEnoblingAnnotation)
    var annotation: String?
) {
    constructor(enobling: Enobling, bindery: Bindery, annotation: String?) : this(null, enobling, bindery, annotation)
}