package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable

@Serializable
sealed class ClientResp {
    abstract val clientId: Int
    abstract val phoneNumber: String?
    abstract val email: String?
}

fun ClientResp.summary(): String {
    when (this) {
        is IndividualClientResp -> return "$name $surname PESEL: $psudoPESEL"
        is CompanyClientResp -> return "$name NIP: $nip"
    }
}