package pl.macia.printinghouse.server.bmodel

import kotlin.reflect.KProperty

internal fun <B : ConvertableAbstract<P>, P, I> delegate(
    be: P?,
    construct: (P) -> B,
    clazz: Class<I>
): Delegate<B, P, I> {
    return Delegate(be, construct, clazz)
}

internal class Delegate<B : ConvertableAbstract<P>, P, I>(var be: P?, val constructor: (P) -> B, val clazz: Class<I>) {
    operator fun getValue(thisRef: Any, property: KProperty<*>): I? {
        return be?.let { clazz.cast(constructor(it)) }
    }

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: I?) {
        be = (value as? B)?.persistent
    }
}