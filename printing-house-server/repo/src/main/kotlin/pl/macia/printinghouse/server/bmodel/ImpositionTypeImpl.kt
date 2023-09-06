package pl.macia.printinghouse.server.bmodel

import pl.macia.printinghouse.server.dto.ImpositionType as PImpositionType

internal class ImpositionTypeImpl(p: PImpositionType) : ImpositionType, BusinessBase<PImpositionType>(p) {

    constructor(name: String) : this(
        PImpositionType(name)
    )

    override var impTypId: Int? by persistent::id
    override var name: String by persistent::name
}

fun ImpositionType(name: String): ImpositionType = ImpositionTypeImpl(name)