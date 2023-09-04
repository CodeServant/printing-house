package pl.macia.printinghouse.server.bmodel

/**
 * Keep in mind that you can't rely on casting or type checking with the [Client] because that would be resource consuming by database.
 */
sealed interface Client {
    var clientId: Int?
    var phoneNumber: String?
    var email: Email?
}