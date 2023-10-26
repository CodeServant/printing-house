package pl.macia.printinghouse.server.bmodel

import pl.macia.printinghouse.server.dto.Employee as PEmployee
import pl.macia.printinghouse.server.dto.Role as PRole

internal class EmployeeImpl(p: PEmployee) : Employee, BusinessBase<PEmployee>(p) {
    override var employed: Boolean by persistent::employed
    override var activeAccount: Boolean by persistent::activeAccount
    override var password: String by persistent::password
    override val roles: MutableSet<Role> = BMutableSet<Role, PRole>(::RoleImpl, {
        it as RoleImpl
        it.persistent
    }, persistent.roles)
    override var email: Email by delegate(persistent::email, ::EmailImpl, Email::class.java)
    override var personId: Int? by persistent::id
    override var psudoPESEL: String by persistent::pseudoPESEL
    override var surname: String by persistent::surname
    override var name: String by persistent::name
}