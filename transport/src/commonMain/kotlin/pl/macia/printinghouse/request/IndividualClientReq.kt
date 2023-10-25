package pl.macia.printinghouse.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(SerialNames.INDIVIDUAL_CLIENT)
data class IndividualClientReq(
    override val phoneNumber: String? = null,
    override val email: String? = null,
    var psudoPESEL: String,
    var surname: String,
    var name: String
) : ClientReq()