package pl.macia.printinghouse.server.bmodel

import pl.macia.printinghouse.server.dto.Salesman as PSalesman

internal class SalesmanImpl(p: PSalesman) : Salesman, BusinessBase<PSalesman>(p) {
    constructor(
        email: EmailImpl,
        password: String,
        activeAccount: Boolean,
        employed: Boolean,
        name: String,
        surname: String,
        pseudoPESEL: String
    ) : this(
        PSalesman(
            email.persistent,
            password,
            activeAccount,
            employed,
            name,
            surname,
            pseudoPESEL
        )
    )

    override var employed: Boolean by persistent::employed
    override var activeAccount: Boolean by persistent::activeAccount
    override var password: String by persistent::password
    override val roles: MutableSet<Role> = toBizRoleSet(persistent.roles)
    override var email: Email by delegate(persistent.email, ::EmailImpl, Email::class.java)
    override var personId: Int? by persistent::id
    override var psudoPESEL: String by persistent::pseudoPESEL
    override var surname: String by persistent::surname
    override var name: String by persistent::name
}

fun Salesman(
    email: Email,
    password: String,
    activeAccount: Boolean,
    employed: Boolean,
    name: String,
    surname: String,
    pseudoPESEL: String
): Salesman {
    val em = email as EmailImpl
    return SalesmanImpl(em, password, activeAccount, employed, name, surname, pseudoPESEL)
}