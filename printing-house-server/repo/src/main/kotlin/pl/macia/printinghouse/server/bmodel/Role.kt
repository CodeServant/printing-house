package pl.macia.printinghouse.server.bmodel

sealed interface Role {
    var roleId: Int?
    var name: String
}