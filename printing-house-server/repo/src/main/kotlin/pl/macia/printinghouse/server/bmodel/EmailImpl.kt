package pl.macia.printinghouse.server.bmodel

import pl.macia.printinghouse.server.dto.Email as PEmail

internal class EmailImpl(persistent: PEmail) : Email, ConvertableAbstract<PEmail>(persistent) {
    constructor(email: String) : this(PEmail(email))

    override var emailId: Int? by persistent::id
    override var email: String by persistent::email
}

fun Email(email: String): Email {
    return EmailImpl(email)
}