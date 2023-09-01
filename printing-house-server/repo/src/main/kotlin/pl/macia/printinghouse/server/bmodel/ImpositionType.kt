package pl.macia.printinghouse.server.bmodel

sealed interface ImpositionType {
    var impTypId: Int?
    var name: String
}