package pl.macia.printinghouse.server.bmodel

import pl.macia.printinghouse.server.dto.Enobling as PEnobling

internal class EnoblingImpl(persistent: PEnobling) : Enobling, BusinessBase<PEnobling>(persistent) {
    constructor(name: String, description: String?) : this(
        PEnobling(name, description)
    )

    override var enoblingId: Int? by persistent::id
    override var name: String by persistent::name
    override var description: String? by persistent::description
}

fun Enobling(name: String, description: String? = null): Enobling {
    return EnoblingImpl(name, description)
}