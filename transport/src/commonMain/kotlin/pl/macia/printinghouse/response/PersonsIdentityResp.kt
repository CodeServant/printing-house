package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable

/**
 * This is data for human to identify the person which we're talking about.
 */
@Serializable
data class PersonsIdentityResp(
    val id: Int,
    val name: String,
    val surname: String
)