package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.bmodel.Printer
import pl.macia.printinghouse.server.bmodel.PrinterImpl
import pl.macia.printinghouse.server.dao.PrinterDAO
import pl.macia.printinghouse.server.dto.Printer as PPrinter

@Repository
internal class PrinterRepoImpl : PrinterIntRepo {

    @Autowired
    private lateinit var dao: PrinterDAO
    private fun PPrinter.toBiz(): Printer {
        return PrinterImpl(this)
    }

    override fun save(obj: Printer): Printer {
        obj as PrinterImpl
        return dao.save(obj.persistent).toBiz()
    }

    override fun findById(id: Int): Printer? {
        return dao.findByIdOrNull(id)?.toBiz()
    }

    override fun findByDigest(digest: String): Printer? {
        return dao.findByDigest(digest)?.toBiz()
    }

    override fun findAll(): List<Printer> {
        return dao.findAll().map { it.toBiz() }
    }
}