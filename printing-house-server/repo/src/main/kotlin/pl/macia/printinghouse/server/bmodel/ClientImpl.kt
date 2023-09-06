package pl.macia.printinghouse.server.bmodel

import pl.macia.printinghouse.server.dto.Client as PClient

internal class ClientImpl(p:PClient) : Client, BusinessBase<PClient>(p) {
    override var clientId: Int? by persistent::id
    override var phoneNumber: String? by persistent::phoneNumber
    override var email: Email? by delegate(persistent.email, ::EmailImpl, Email::class.java)
}