package pl.macia.printinghouse.server.bmodel

interface Company : Client {
    var companyId: Int?
    var nip: String
    var name: String
}