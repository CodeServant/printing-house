package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable

@Serializable
sealed class ClientReq {
    abstract val phoneNumber: String?
    abstract val email: String?
}

fun ClientReq.summary(): String {
    when (this) {
        is IndividualClientReq -> return "$name $surname PESEL: $psudoPESEL"
        is CompanyClientReq -> return "$name NIP: $nip"
    }
}