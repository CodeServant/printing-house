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
class PrinterRepoImplTest {

    @Autowired
    private lateinit var repo: PrinterIntRepo

    @Test
    fun `find by id`() {
        repo.findById(1)?.apply {
            assertEquals("duża komori", name)
            assertEquals("DK", digest)
        }
        repo.findById(2)?.apply {
            assertEquals("mała komori", name)
            assertEquals("MK", digest)
        }
    }

    @Test
    @Transactional
    fun `create new`() {
        val new = Printer("wielka komorii", "WK")
        SingleIdTests<Printer, Int>(repo).createNew(new, new::printerId, repo::findById)
    }
}