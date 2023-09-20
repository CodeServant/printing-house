package pl.macia.printinghouse.server.bmodel

sealed interface Enobling {
    var enoblingId: Int?
    var name: String
    var description: String?
}

internal interface EnoblingBusinessBase {
    val persistent: pl.macia.printinghouse.server.dto.Enobling
}

internal sealed interface EnoblingInt : Enobling, EnoblingBusinessBase