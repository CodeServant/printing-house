package pl.macia.printinghouse.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("CompanyClient")
data class CompanyClientResp(
    override val clientId: Int,
    override val phoneNumber: String?,
    override val email: String?,
    var companyId: Int,
    var nip: String,
    var name: String,
) : ClientResp()