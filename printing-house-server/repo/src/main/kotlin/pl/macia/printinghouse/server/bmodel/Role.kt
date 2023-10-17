package pl.macia.printinghouse.server.bmodel

/**
 * Roles are managed by the system and shouldn't be accessed by the users.
 */
sealed interface Role {
    var roleId: Int?
    var name: String
}