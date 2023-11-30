package pl.macia.printinghouse.server.services

import kotlin.reflect.KMutableProperty

/**
 * Utilities for common change pattern in services. Generally create [ChangeTracker] instance and run different assigning functions to record changes.
 */
class ChangeTracker(val nullingRest: Boolean = false) {
    var changed: Boolean = false
        protected set

    /**
     * Changes [assignTo] property with the [value] value if it exists.
     */
    fun <E> applyChange(value: E?, assignTo: KMutableProperty<E>) {
        val areEquals = value == assignTo.getter.call()
        val norNullingRestNotNullable = (!nullingRest || !assignTo.returnType.isMarkedNullable)
        if (
            areEquals ||
            value == null && norNullingRestNotNullable
        ) return
        assignTo.setter.call(value)
        changed = true
    }
}