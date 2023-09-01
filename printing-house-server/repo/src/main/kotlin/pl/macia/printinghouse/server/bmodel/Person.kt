package pl.macia.printinghouse.server.bmodel

sealed interface Person {
    var personId: Int?
    var psudoPESEL: String
    var surname: String
    var name: String
}