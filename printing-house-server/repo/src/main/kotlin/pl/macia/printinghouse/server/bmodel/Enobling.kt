package pl.macia.printinghouse.server.bmodel

sealed interface Enobling {
    var enoblingId: Int?
    var name: String
    var description: String?
}