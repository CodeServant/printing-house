package pl.macia.printinghouse.server.bmodel

import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
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

    constructor(named: PSize) : this(
        named, null, null
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

    override var sizeId: Int?
        get() = persistent?.id
        set(value) {
            persistent?.id = value
        }

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

internal fun delegateSizeImpl(
    size: KMutableProperty<PSize?>,
    width: KMutableProperty<Double?>,
    height: KMutableProperty<Double?>
): NonNullSizeImplDelegate {
    return NonNullSizeImplDelegate(size, width, height)
}

internal class NonNullSizeImplDelegate(
    val size: KMutableProperty<PSize?>,
    val width: KMutableProperty<Double?>,
    val height: KMutableProperty<Double?>,
) {
    operator fun getValue(thisRef: Any, property: KProperty<*>): Size {
        return if (size.getter.call() != null) SizeImpl(size.getter.call()!!) else SizeImpl(
            width.getter.call()!!,
            height.getter.call()!!
        )
    }

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: Size) {
        fun setNew(newSize: SizeImpl) {
            size.setter.call(newSize.persistent)
            width.setter.call(newSize.width)
            height.setter.call(newSize.heigth)
        }

        val idIsNull = value.sizeId == null
        val nameIsNull = value.name == null
        if (idIsNull && nameIsNull) {
            setNew(SizeImpl(value.width, value.heigth))
        } else if (idIsNull && !nameIsNull) {
            setNew(SizeImpl(value.name!!, value.width, value.heigth))
        } else if (!idIsNull && !nameIsNull) {
            setNew(value as SizeImpl)
        }
    }
}