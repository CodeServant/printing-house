package pl.macia.printinghouse.server.test.dao

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.PrinterDAO
import pl.macia.printinghouse.server.dto.Printer
import kotlin.jvm.optionals.getOrNull

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class PrinterDAOTest {
    @Autowired
    lateinit var dao: PrinterDAO

    @Test
    fun `find by id test`() {
        val printer = dao.findById(1).getOrNull()!!
        assertEquals("DK", printer.digest)
        assertEquals("duża komori", printer.name)
    }

    @Test
    @Order(2)
    fun `create new test`() {
        val newP = Printer("Kolejna Komorii", "KK")
        assertDoesNotThrow {
            dao.saveAndFlush(newP)
        }
        assertNotNull(newP.id)
    }

    @Test
    @Order(3)
    fun `delete one test`() {
        var kk = dao.findByDigest("KK")
        assertNotNull(kk)
        dao.delete(kk!!)
        kk = dao.findByDigest("KK")
        assertNull(kk)
    }
}