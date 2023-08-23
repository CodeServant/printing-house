package pl.macia.printinghouse.server.bmodel

interface Employee : Person {
    var employed: Boolean
    var activeAccount: Boolean
    var password: String
    var roles: Set<Role>
    var email: Email
}