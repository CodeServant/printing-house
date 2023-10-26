package pl.macia.printinghouse.server.bmodel

import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty

/**
 * @param P persistance object to be delegated to
 * @param be target persistance entity to delegate the property to
 * @param construct constructor of type [BusinessBase]
 * @param clazz class of interface to be cast to
 */
internal fun <B : BusinessBase<P>, P, I> delegate(
    be: KMutableProperty<P?>,
    construct: (P) -> B,
    clazz: Class<I>
): Delegate<B, P, I> {
    return Delegate(be, construct, clazz)
}

internal fun <B : BusinessBase<P>, P, I> delegate(
    be: KMutableProperty<P>,
    construct: (P) -> B,
    clazz: Class<I>
): NonNullDelegate<B, P, I> {
    return NonNullDelegate(be, construct, clazz)
}

internal class Delegate<B : BusinessBase<P>, P, I>(
    val be: KMutableProperty<P?>,
    val constructor: (P) -> B,
    val clazz: Class<I>
) {
    operator fun getValue(thisRef: Any, property: KProperty<*>): I? {
        return be.call()?.let { clazz.cast(constructor(it)) }
    }

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: I?) {
        be.setter.call((value as? B)?.persistent)
    }
}

internal class NonNullDelegate<B : BusinessBase<P>, P, I>(
    var be: KMutableProperty<P>,
    val constructor: (P) -> B,
    val clazz: Class<I>
) {
    operator fun getValue(thisRef: Any, property: KProperty<*>): I {
        return be.call().let { clazz.cast(constructor(it)) }
    }

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: I) {
        be.setter.call((value as B).persistent)
    }
}