package pl.macia.printinghouse.server.bmodel

/**
 * Instances of a [Client] are expected to be disjoint.
 */
sealed interface Client {
    var clientId: Int?
    var phoneNumber: String?
    var email: Email?
}