package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable

@Serializable
data class NewRecID(
    val id: Long
)