package pl.macia.printinghouse.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("IndividualClient")
data class IndividualClientResp(
    override val clientId: Int,
    override val phoneNumber: String?,
    override val email: String?,
    var personId: Int,
    var psudoPESEL: String,
    var surname: String,
    var name: String
) : ClientResp()