package pl.macia.printinghouse.server.test.dao

import jakarta.transaction.Transactional
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.OrderDAO
import pl.macia.printinghouse.server.dao.OrderEnoblingDAO
import pl.macia.printinghouse.server.dao.PaperOrderTypeDAO
import pl.macia.printinghouse.server.dao.WorkflowStageStopDAO
import java.math.BigDecimal
import java.time.LocalDateTime

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class OrderDAOTest {
    @Autowired
    lateinit var dao: OrderDAO

    @Autowired
    lateinit var daoWorkflowStageStop: WorkflowStageStopDAO

    @Autowired
    lateinit var daoPaperOrderType: PaperOrderTypeDAO

    @Autowired
    lateinit var daoOrderEnobling: OrderEnoblingDAO

    @Test
    fun `find single by id`() {
        val order = dao.findByIdOrNull(1)!!
        assertEquals("gorzelska", order.name)
        assertEquals(LocalDateTime.of(2022, 10, 21, 0, 0), order.creationDate)
        assertEquals(205.0, order.netSize.width)
        assertEquals("Kowalski-Salesman", order.supervisor.surname)
        assertEquals("maklowicz@wp.pl", order.client.email?.email)
        assertEquals("Karton", order.bindingForm.name)
        assertEquals("A3", order.bindery.name)
        assertFalse(order.imageURL?.url?.isBlank()!!)
        assertTrue(order.checked)
        assertNull(order.completionDate)
        assertNull(order.withdrawalDate)
        assertNull(order.comment)
        assertTrue(BigDecimal(1000).compareTo(order.calculationCard?.bindingCost) == 0)
    }

    @Test
    @Transactional
    fun `delete single`() {
        val order = dao.findByIdOrNull(1)!!
        val workflowStageStop = order.workflowStageStops.first()
        val orderEnobling = order.orderEnoblings.first()
        val paperOrderType = order.paperOrderTypes.first()
        dao.delete(order)
        assertNull(dao.findByIdOrNull(1))
        assertNull(daoWorkflowStageStop.findByIdOrNull(workflowStageStop.id))
        assertNull(daoOrderEnobling.findByIdOrNull(orderEnobling.id))
        assertNull(daoPaperOrderType.findByIdOrNull(paperOrderType.id))
    }
}