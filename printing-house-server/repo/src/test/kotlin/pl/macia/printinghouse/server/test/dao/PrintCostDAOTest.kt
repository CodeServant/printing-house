package pl.macia.printinghouse.server.test.dao

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.PrintCostDAO

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
internal class PrintCostDAOTest {
    @Autowired
    lateinit var dao: PrintCostDAO

    @Test
    fun `find by id`() {
        val printCost = dao.findByIdOrNull(1)!!
        assertEquals(12, printCost.printCost.toInt())
        assertEquals(1000, printCost.calculationCard.bindingCost.toInt())
        assertEquals("DK", printCost.printer.digest)
    }
}