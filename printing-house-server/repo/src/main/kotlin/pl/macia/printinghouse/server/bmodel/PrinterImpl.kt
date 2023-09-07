package pl.macia.printinghouse.server.bmodel

import pl.macia.printinghouse.server.dto.Printer as PPrinter

internal class PrinterImpl(persistent: PPrinter) : Printer, BusinessBase<PPrinter>(persistent) {
    constructor(name: String, digest: String) : this(
        PPrinter(
            name,
            digest
        )
    )

    override var printerId: Int? by persistent::id
    override var name: String by persistent::name
    override var digest: String by persistent::digest
}

fun Printer(name: String, digest: String): Printer = PrinterImpl(name, digest)