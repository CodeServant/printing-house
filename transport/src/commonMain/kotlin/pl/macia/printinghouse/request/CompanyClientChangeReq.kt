package pl.macia.printinghouse.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(SerialNames.COMPANY_CLIENT)
data class CompanyClientChangeReq(
    override val nullingRest: Boolean = false,
    override val phoneNumber: String? = null,
    override val email: String? = null,
    val nip: String? = null,
    val name: String? = null,
) : ClientChangeReq()