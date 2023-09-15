package pl.macia.printinghouse.server.bmodel

import pl.macia.printinghouse.server.dto.Role as PRole

internal class RoleImpl(persistent: PRole) : Role, BusinessBase<PRole>(persistent) {
    constructor(name: String) : this(
        PRole(name)
    )

    override var roleId: Int? by persistent::id
    override var name: String by persistent::name
}

fun Role(name: String): Role {
    return RoleImpl(name)
}

internal fun toBizRoleSet(roles: MutableSet<PRole>): BMutableSet<Role, PRole> {
    return BMutableSet(::RoleImpl, {
        it as RoleImpl
        it.persistent
    }, roles)
}