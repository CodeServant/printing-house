package pl.macia.printinghouse.server.bmodel

sealed interface PaperType {
    var papTypeId: Int?
    var name: String
    var shortName: String
}