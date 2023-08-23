package pl.macia.printinghouse.server.bmodel

interface Printer {
    var printerId: Int?
    var name: String
    var digest: String
}