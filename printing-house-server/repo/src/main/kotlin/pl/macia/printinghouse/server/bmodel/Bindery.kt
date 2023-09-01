package pl.macia.printinghouse.server.bmodel

sealed interface Bindery {
    var binderyId: Int?
    var name: String
}