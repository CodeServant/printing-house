package pl.macia.printinghouse.server.bmodel

import pl.macia.printinghouse.server.dto.Bindery as PBindery

internal class BinderyImpl(persistent: PBindery) : Bindery, BusinessBase<PBindery>(persistent) {
    constructor(name: String) : this(PBindery(name))

    override var binderyId: Int? by persistent::id
    override var name: String by persistent::name
}

fun Bindery(name: String): Bindery {
    return BinderyImpl(name)
}