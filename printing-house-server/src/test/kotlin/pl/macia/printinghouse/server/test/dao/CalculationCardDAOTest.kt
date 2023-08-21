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
import java.math.BigDecimal

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
class CalculationCardDAOTest {
    @Autowired
    lateinit var dao: CalculationCardDAO

    @Autowired
    lateinit var daoOrder: OrderDAO

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
}