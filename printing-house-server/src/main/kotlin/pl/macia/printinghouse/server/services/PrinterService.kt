package pl.macia.printinghouse.server.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.macia.printinghouse.converting.ConversionException
import pl.macia.printinghouse.response.PrinterResp
import pl.macia.printinghouse.server.bmodel.Printer
import pl.macia.printinghouse.server.repository.PrinterRepo

@Service
class PrinterService {
    @Autowired
    lateinit var repo: PrinterRepo

    fun findById(id: Int): PrinterResp? {
        return repo.findById(id)?.toTransport()
    }

    fun allPrinters(): List<PrinterResp> {
        return repo.findAll().map { it.toTransport() }
    }
}

private fun Printer.toTransport(): PrinterResp {
    return PrinterResp(
        id = printerId
            ?: throw ConversionException("${Printer::class.simpleName} can't be converted the ${Printer::printerId.name} is null"),
        name = name,
        digest = digest
    )
}