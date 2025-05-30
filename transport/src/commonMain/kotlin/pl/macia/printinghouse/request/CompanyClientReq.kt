package pl.macia.printinghouse.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(SerialNames.COMPANY_CLIENT)
data class CompanyClientReq(
    override val phoneNumber: String? = null,
    override val email: String? = null,
    val nip: String,
    val name: String
) : ClientReq()