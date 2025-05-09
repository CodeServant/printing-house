package pl.macia.printinghouse.server.bmodel

import pl.macia.printinghouse.server.dto.OrderEnobling as POrderEnobling

internal class OrderEnoblingImpl(p: POrderEnobling) : OrderEnobling, BusinessBase<POrderEnobling>(p) {
    constructor(enobling: EnoblingInt, bindery: BinderyImpl, order: OrderImpl, annotation: String?) : this(
        POrderEnobling(
            enobling = enobling.persistent,
            bindery = bindery.persistent,
            order = order.persistent,
            annotation = annotation
        )
    )

    override var orderEnobid: Int? by persistent::id
    override var annotation: String? by persistent::annotation
    override var enobling: Enobling by delegate(persistent::enobling, ::EnoblingImpl, Enobling::class.java)
    override var bindery: Bindery by delegate(persistent::bindery, ::BinderyImpl, Bindery::class.java)
    override val order: Order by delegate(persistent::order, ::OrderImpl, Order::class.java)
}

internal fun toBizOrderEnobling(list: MutableList<POrderEnobling>): BMutableList<OrderEnobling, POrderEnobling> {
    return BMutableList(::OrderEnoblingImpl, {
        it as OrderEnoblingImpl
        it.persistent
    }, list)
}