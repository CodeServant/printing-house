package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable

/**
 * Record ID. Usually used as a response to new object creation.
 */
@Serializable
data class RecID(
    val id: Long
) {
    /**
     * @throws LongToNumException if the conversion to smaller size value is impossible
     */
    @Throws(LongToNumException::class)
    fun asInt(): Int {
        if (id < Int.MIN_VALUE || id > Int.MAX_VALUE)
            throw LongToNumException("${id::class.simpleName} should be between ${Int.MIN_VALUE} and ${Int.MAX_VALUE}")
        return id.toInt()
    }

}

class LongToNumException(str: String) : Exception(str)