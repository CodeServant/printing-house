package pl.macia.printinghouse.server.bmodel

import pl.macia.printinghouse.server.dto.PaperType as PPaperType

internal class PaperTypeImpl(persistent: PPaperType) : PaperType, BusinessBase<PPaperType>(persistent) {
    constructor(name: String, shortName: String) : this(
        PPaperType(name, shortName)
    )

    override var papTypeId: Int? by persistent::id
    override var name: String by persistent::name
    override var shortName: String by persistent::shortName
}

fun PaperType(name: String, shortName: String): PaperType {
    return PaperTypeImpl(name, shortName)
}