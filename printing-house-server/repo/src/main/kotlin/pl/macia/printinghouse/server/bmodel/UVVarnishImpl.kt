package pl.macia.printinghouse.server.bmodel

import pl.macia.printinghouse.server.dto.UVVarnish as PUVVarnish

internal class UVVarnishImpl(p: PUVVarnish) : UVVarnish, ConvertableAbstract<PUVVarnish>(p) {
    constructor(name: String, description: String?) : this(
        PUVVarnish(name, description)
    )

    override var enoblingId: Int? by persistent::id
    override var name: String by persistent::name
    override var description: String? by persistent::description
}

fun UVVarnish(name: String, description: String? = null): UVVarnish {
    return UVVarnishImpl(name, description)
}