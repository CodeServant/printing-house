package pl.macia.printinghouse.server.bmodel

import kotlin.reflect.KProperty

/**
 * @param P persistance object to be delegated to
 * @param be target persistance entity to delegate the property to
 * @param construct constructor of type [BusinessBase]
 * @param clazz class of interface to be cast to
 */
internal fun <B : BusinessBase<P>, P, I> delegate(
    be: P?,
    construct: (P) -> B,
    clazz: Class<I>
): Delegate<B, P, I> {
    return Delegate(be, construct, clazz)
}

internal class Delegate<B : BusinessBase<P>, P, I>(var be: P?, val constructor: (P) -> B, val clazz: Class<I>) {
    operator fun getValue(thisRef: Any, property: KProperty<*>): I? {
        return be?.let { clazz.cast(constructor(it)) }
    }

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: I?) {
        be = (value as? B)?.persistent
    }
}