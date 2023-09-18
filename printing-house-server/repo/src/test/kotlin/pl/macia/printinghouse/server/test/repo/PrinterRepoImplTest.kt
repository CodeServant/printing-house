package pl.macia.printinghouse.server.test.repo

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.bmodel.Printer
import pl.macia.printinghouse.server.repository.PrinterIntRepo

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
internal class PrinterRepoImplTest {

    @Autowired
    private lateinit var repo: PrinterIntRepo

    @Test
    fun `find by id`() {
        var prntr = repo.findById(1)
        assertEquals("duża komori", prntr?.name)
        assertEquals("DK", prntr?.digest)
        prntr = repo.findById(2)
        assertEquals("mała komori", prntr?.name)
        assertEquals("MK", prntr?.digest)
        prntr = repo.findById(100)
        assertNull(prntr)
    }

    @Test
    @Transactional
    fun `create new`() {
        val new = Printer("wielka komorii", "WK")
        SingleIdTests<Printer, Int>(repo).createNew(new, new::printerId, repo::findById)
    }
}