package pl.macia.printinghouse.server.bmodel

sealed interface OrderEnobling {
    var orderEnobid: Int?
    var annotation: String?
    var enobling: Enobling
    var bindery: Bindery
}