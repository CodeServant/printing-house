package pl.macia.printinghouse.server.bmodel

import pl.macia.printinghouse.server.dto.Punch as PPunch

internal class PunchImpl(persistent: PPunch) : Punch, ConvertableAbstract<PPunch>(persistent) {
    constructor(name: String, description: String?) : this(
        PPunch(name, description)
    )

    override var enoblingId: Int? by persistent::id
    override var name: String by persistent::name
    override var description: String? by persistent::description
}

fun Punch(name: String, description: String? = null): Punch {
    return PunchImpl(name, description)
}