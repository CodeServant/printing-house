package pl.macia.printinghouse.server.bmodel

sealed interface CompanyClient : Client {
    var companyId: Int?
    var nip: String
    var name: String
}