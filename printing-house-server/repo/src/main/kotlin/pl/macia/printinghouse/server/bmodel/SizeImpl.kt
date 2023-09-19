package pl.macia.printinghouse.server.bmodel

import pl.macia.printinghouse.server.dto.Size as PSize

internal class SizeImpl(persistent: PSize) : Size, BusinessBase<PSize>(persistent) {
    constructor(name: String, width: Double, heigth: Double) : this(
        PSize(name, width, heigth)
    )

    constructor(width: Double, heigth: Double) : this(
        PSize(width, heigth)
    )

    override var sizeId: Int? by persistent::id
    override var name: String? by persistent::name
    override var heigth: Double by persistent::heigth
    override var width: Double by persistent::width
}

fun Size(name: String, width: Number, heigth: Number): Size = SizeImpl(name, width.toDouble(), heigth.toDouble())
fun Size(width: Number, heigth: Number): Size = SizeImpl(width.toDouble(), heigth.toDouble())