package pl.macia.printinghouse.server.services

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.macia.printinghouse.converting.ConversionException
import pl.macia.printinghouse.request.PrinterChangeReq
import pl.macia.printinghouse.request.PrinterReq
import pl.macia.printinghouse.response.ChangeResp
import pl.macia.printinghouse.response.PrinterResp
import pl.macia.printinghouse.response.RecID
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

    @Transactional
    fun insertNew(req: PrinterReq): RecID {
        val newId = repo.save(
            Printer(
                name = req.name,
                digest = req.digest
            )
        )
        return RecID(
            newId.printerId?.toLong()
                ?: throw Exception("some error while saving ${Printer::class.simpleName} to database")
        )
    }

    @Transactional
    fun changePrinter(id: Int, changeReq: PrinterChangeReq): ChangeResp? {
        val found = repo.findById(id) ?: return null
        val changeTracker = ChangeTracker()
        changeTracker.apply {
            applyChange(changeReq.name, found::name)
            applyChange(changeReq.digest, found::digest)
        }
        return ChangeResp(changeTracker.changed)
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