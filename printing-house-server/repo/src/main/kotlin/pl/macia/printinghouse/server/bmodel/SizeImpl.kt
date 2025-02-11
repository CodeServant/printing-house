package pl.macia.printinghouse.server.bmodel

import pl.macia.printinghouse.server.dto.Size as PSize

internal class SizeImpl private constructor(
    var persistent: PSize?,
    cWidth: Double?,
    cHeight: Double?
) : Size {
    constructor(name: String, width: Double, heigth: Double) : this(
        PSize(name, width, heigth),
        cWidth = null,
        cHeight = null
    )

    constructor(width: Double, heigth: Double) : this(
        null, width, heigth
    )

    override var width: Double = persistent?.width ?: cWidth
    ?: throw NullPointerException("Size implementation error, there is no ${this::sizeId.name} value")
        get() {
            return if (persistent != null) {
                persistent!!.width
            } else {
                field
            }
        }
        set(value) {
            if (persistent != null) {
                persistent?.width = value
            } else {
                field = value
            }
        }

    override var heigth: Double = persistent?.heigth ?: cHeight
    ?: throw NullPointerException("Size implementation error, there is no ${this::heigth.name} value")
        get() {
            return if (persistent != null) {
                persistent!!.heigth
            } else {
                field
            }
        }
        set(value) {
            if (persistent != null) {
                persistent?.heigth = value
            } else {
                field = value
            }
        }

    override var sizeId: Int? = null
    override var name: String?
        get() {
            return persistent?.name
        }
        set(value) {
            if (value != null)
                persistent?.name = value
        }


}

fun Size(name: String, width: Number, heigth: Number): Size = SizeImpl(name, width.toDouble(), heigth.toDouble())
fun Size(width: Number, heigth: Number): Size = SizeImpl(width.toDouble(), heigth.toDouble())