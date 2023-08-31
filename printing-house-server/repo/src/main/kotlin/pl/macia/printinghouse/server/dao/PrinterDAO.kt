package pl.macia.printinghouse.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import pl.macia.printinghouse.server.dto.Printer

internal interface PrinterDAO : JpaRepository<Printer, Int>, PrinterDAOCustom {
    fun findByDigest(digest: String): Printer?
}