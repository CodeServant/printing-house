package pl.macia.printinghouse.server.bmodel

import pl.macia.printinghouse.server.dto.Client
import pl.macia.printinghouse.server.dto.Company

/**
 * @throws NullPointerException when in [persistent] does not have defined client
 */
internal class CompanyClientImpl(persistent: Company) : CompanyClient, BusinessBase<Company>(persistent) {
    constructor(name: String, nip: String, email: EmailImpl?, phoneNumber: String?) : this(
        Company(
            name,
            nip,
            Client(
                email?.persistent,
                phoneNumber
            )
        )
    )

    override var companyId: Int? by persistent::id
    override var nip: String by persistent::nip
    override var name: String by persistent::name
    override var clientId: Int? by persistent.client!!::id
    override var phoneNumber: String? by persistent.client!!::phoneNumber
    override var email: Email? by Delegate(persistent.client?.email, ::EmailImpl, Email::class.java)
}

fun CompanyClient(name: String, nip: String, email: Email?, phoneNumber: String?): CompanyClient {
    return CompanyClientImpl(name, nip, email as? EmailImpl, phoneNumber)
}