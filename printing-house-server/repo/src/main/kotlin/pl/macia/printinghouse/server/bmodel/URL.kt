package pl.macia.printinghouse.server.bmodel

sealed interface URL {
    var urlId: Long?
    var url: String
}