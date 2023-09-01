package pl.macia.printinghouse.server.bmodel

sealed interface Client {
    var clientId: Int?
    var phoneNumber: String?
    var email: Email?
}