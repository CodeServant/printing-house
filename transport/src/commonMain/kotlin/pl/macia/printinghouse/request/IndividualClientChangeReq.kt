package pl.macia.printinghouse.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(SerialNames.INDIVIDUAL_CLIENT)
data class IndividualClientChangeReq(
    override val nullingRest: Boolean = false,
    override val phoneNumber: String? = null,
    override val email: String? = null,
    var psudoPESEL: String? = null,
    var surname: String? = null,
    var name: String? = null,
) : ClientChangeReq()