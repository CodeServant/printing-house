package pl.macia.printinghouse.server.bmodel

sealed interface Enobling {
    var enoblingId: Int?
    var name: String
    var description: String?
}

internal interface EnoblingBusinessBase {
    val persistent: pl.macia.printinghouse.server.dto.Enobling
}

/**
 * Interface to enable type casting to internal [Enobling] from its subtypes.
 */
internal sealed interface EnoblingInt : Enobling, EnoblingBusinessBase