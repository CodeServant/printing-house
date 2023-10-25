package pl.macia.printinghouse.server.bmodel

import pl.macia.printinghouse.server.dto.Client
import pl.macia.printinghouse.server.dto.IndividualClient as PInCli

internal class IndividualClientImpl(persistent: PInCli) : IndividualClient,
    BusinessBase<PInCli>(persistent) {

    constructor(
        phoneNumber: String?,
        email: EmailImpl?,
        psudoPESEL: String,
        surname: String,
        name: String
    ) : this(
        PInCli(
            name = name,
            surname = surname,
            pseudoPESEL = psudoPESEL,
            client = Client(
                email = email?.persistent,
                phoneNumber = phoneNumber
            )
        )
    )

    override var clientId: Int? by persistent.client::id
    override var phoneNumber: String? by persistent.client::phoneNumber
    override var email: Email? by delegate(persistent.client.email, ::EmailImpl, Email::class.java)
    override var personId: Int? by persistent::id
    override var psudoPESEL: String by persistent::pseudoPESEL
    override var surname: String by persistent::surname
    override var name: String by persistent::name
}

fun IndividualClient(
    phoneNumber: String?,
    email: Email?,
    psudoPESEL: String,
    surname: String,
    name: String
): IndividualClient {
    return IndividualClientImpl(
        phoneNumber,
        email as? EmailImpl,
        psudoPESEL,
        surname,
        name
    )
}