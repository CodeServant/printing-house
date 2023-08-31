package pl.macia.printinghouse.server.test.dao

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.CalculationCardDAO
import pl.macia.printinghouse.server.dao.OrderDAO
import pl.macia.printinghouse.server.dao.PrintCostDAO
import pl.macia.printinghouse.server.dao.PrinterDAO
import java.math.BigDecimal

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
internal class CalculationCardDAOTest {
    @Autowired
    private lateinit var daoPrintCost: PrintCostDAO

    @Autowired
    private lateinit var dao: CalculationCardDAO

    @Autowired
    private lateinit var daoOrder: OrderDAO

    @Autowired
    private lateinit var daoPrinter: PrinterDAO

    @Test
    fun `find by id test`() {
        val calcCard = dao.findByIdOrNull(1)!!
        assertNotNull(calcCard.id)
        assertTrue(BigDecimal(1000).compareTo(calcCard.bindingCost) == 0)
        assertNotNull(calcCard.order)
        assertEquals("Karton", calcCard.order.bindingForm.name)
    }

    @Test
    @Transactional
    fun `delete one test`() {
        val order = daoOrder.findByIdOrNull(1)!!
        val init = dao.count()
        order.calculationCard = null
        assertEquals(init - 1, dao.count())
    }

    @Test
    @Transactional
    fun `change one`() {
        val order = daoOrder.findByIdOrNull(1)!!
        order.calculationCard = null
        daoOrder.saveAndFlush(order)
        var calc = order.setCalculationCard(BigDecimal(100), BigDecimal(101), BigDecimal(102), BigDecimal(103))
        calc.addPrintCost(daoPrinter.findByDigest("DK")!!, BigDecimal(100), BigDecimal(130))
        daoOrder.saveAndFlush(order)
        calc = dao.findByIdOrNull(1)!!
        assertEquals(100, calc.bindingCost.toInt())
        assertEquals(101, calc.enobling.toInt())
        assertEquals(102, calc.otherCosts.toInt())
        assertEquals(103, calc.transport.toInt())
    }

    @Test
    @Transactional
    fun `print cost association test`() {
        val calc = dao.findByIdOrNull(1)!!
        val initSize = calc.printCosts.size
        calc.addPrintCost(
            daoPrinter.findByDigest("MK")!!,
            BigDecimal(10),
            BigDecimal(9)
        )
        assertEquals(initSize + 1, calc.printCosts.size)
        assertEquals(initSize + 1, daoPrintCost.findAll().filter { it.calculationCard.id == calc.id }.size)
        println(calc.printCosts.size)
        calc.printCosts.removeAt(1)
        assertEquals(initSize, calc.printCosts.size)
    }
}