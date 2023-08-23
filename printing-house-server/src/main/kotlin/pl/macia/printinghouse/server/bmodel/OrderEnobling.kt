package pl.macia.printinghouse.server.bmodel

interface OrderEnobling {
    var orderEnobid: Int?
    var annotation: String?
    var enobling: Enobling
    var bindery: Bindery
}