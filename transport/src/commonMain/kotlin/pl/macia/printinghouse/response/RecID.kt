package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable

/**
 * Record ID.
 */
@Serializable
data class RecID(
    val id: Long
)