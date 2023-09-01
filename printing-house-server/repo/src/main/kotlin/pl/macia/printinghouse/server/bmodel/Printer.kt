package pl.macia.printinghouse.server.bmodel

sealed interface Printer {
    var printerId: Int?
    var name: String
    var digest: String
}