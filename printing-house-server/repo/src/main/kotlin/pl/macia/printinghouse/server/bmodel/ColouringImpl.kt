package pl.macia.printinghouse.server.bmodel

import pl.macia.printinghouse.server.dto.Colouring as PColouring

internal class ColouringImpl(persistent: PColouring) : Colouring, BusinessBase<PColouring>(persistent) {
    constructor(firstSide: Byte, secondSide: Byte) : this(
        PColouring(firstSide, secondSide)
    )

    override var colouringId: Byte? by persistent::id
    override var secondSide: Byte by persistent::secondSide
    override var firstSide: Byte by persistent::firstSide
}

fun Colouring(firstSide: Byte, secondSide: Byte): Colouring {
    return ColouringImpl(firstSide, secondSide)
}