package pl.macia.printinghouse.server.test.dao

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.*
import pl.macia.printinghouse.server.dto.*
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

    @Autowired
    lateinit var salesmanDAO: SalesmanDAO

    @Autowired
    lateinit var clientDAO: ClientDAO

    @Autowired
    lateinit var bindingFormDAO: BindingFormDAO

    @Autowired
    lateinit var binderyDAO: BinderyDAO

    @Autowired
    private lateinit var workerDAO: WorkerDAO

    @Autowired
    private lateinit var workflowDirGraphDAO: WorkflowDirGraphDAO

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

    @Test
    @Transactional
    fun `create new one`() {
        val ord = Order(
            name = "createNewOneOrderDAOTest",
            netSize = Size(10.1, 12.2),
            pages = 21,
            supervisor = salesmanDAO.findByIdOrNull(1)!!,
            client = clientDAO.findByIdOrNull(1)!!,
            creationDate = LocalDateTime.now(),
            realizationDate = LocalDateTime.now(),
            bindingForm = bindingFormDAO.findByIdOrNull(1)!!,
            bindery = binderyDAO.findByName("A1")!!,
            folding = true,
            towerCut = true,
            imageURL = Image("https://www.example.com", "some comment"),
            checked = true,
            designsNumberForSheet = 2,
            completionDate = LocalDateTime.now(),
            withdrawalDate = LocalDateTime.now(),
            comment = null,
            calculationCard = null,
        )
        ord.addWorkflowStageStop(
            comment = null,
            createTime = LocalDateTime.now(),
            assignTime = LocalDateTime.now(),
            null,
            worker = workerDAO.findByIdOrNull(2)!!,
            workflowDirEdge = workflowDirGraphDAO.findByIdOrNull(1)!!.edges[0]
        )
        ord.addPaperOrderType(
            paperType = PaperType(
                name = "some new paper",
                shortName = "somPapNew"
            ),
            grammage = 12.0,
            colours = Colouring(1, 0),
            circulation = 1,
            stockCirculation = 1,
            sheetNumber = 1,
            comment = null,
            printer = Printer("some bigger printer", "SBP"),
            platesQuantityForPrinter = 1,
            imposition = ImpositionType("impositoin"),
            size = Size(120.0, 120.0),
            productionSize = Size(120.0, 120.0)
        )
        dao.saveAndFlush(ord)
        assertNotNull(ord.id)
        assertEquals(2, dao.count())
    }

    @Test
    fun `find not assigned by workflow stage id`() {
        fun checkVariant(areChecked: Boolean?) {
            val found = dao.notAssignedWorkflowStage(3, areChecked)
            assertEquals(1, found.size)
            assertEquals("gorzelska", found.first().name)
        }
        checkVariant(null)
        checkVariant(true)
        assertEquals(0, dao.notAssignedWorkflowStage(3, false).size)
    }
}