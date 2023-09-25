package pl.macia.printinghouse.server.test.repo

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.bmodel.*
import pl.macia.printinghouse.server.repository.*
import java.math.BigDecimal
import java.time.LocalDateTime

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
internal class OrderRepoTest {
    @Autowired
    lateinit var repo: OrderIntRepo

    @Autowired
    lateinit var clientRepo: ClientIntRepo

    @Autowired
    lateinit var binderyRepo: BinderyIntRepo

    @Autowired
    lateinit var salesmanRepo: SalesmanIntRepo

    @Autowired
    lateinit var bindingFormRepo: BindingFormIntRepo

    @Autowired
    lateinit var workflowStageRepo: WorkflowStageIntRepo

    @Autowired
    lateinit var uvVarnishRepo: UVVarnishIntRepo

    @Autowired
    lateinit var printerRepo: PrinterIntRepo

    @Autowired
    lateinit var paperTypeRepo: PaperTypeIntRepo

    @Autowired
    lateinit var colouringRepo: ColouringIntRepo

    @Autowired
    lateinit var impositionTypeRepo: ImpositionTypeIntRepo

    @Test
    fun `find by id test`() {
        val found = repo.findById(1)!!
        assertEquals("gorzelska", found.name)
        assertEquals(205.0, found.netSize.width)
        assertEquals(8, found.pages)
        assertEquals("Jan", found.salesman.name)
        assertEquals("maklowicz@wp.pl", found.client.email?.email)
        assertEquals("Karton", found.bindingForm.name)
        assertEquals("A3", found.bindery.name)
        assertEquals(false, found.folding)
        assertEquals(true, found.towerCut)
        assertTrue(found.imageUrl?.url?.isNotEmpty() ?: false)
        assertEquals(
            "draw.io to fajna strona do rysowania diagramów, tego linka można potem odtworzyć i podejrzeć a nawet pozmieniać obrazek",
            found.imageUrl?.imageComment
        )
        assertEquals(true, found.checked)
        assertEquals(4, found.designsNumberForSheet)
        assertNull(found.completionDate)
        assertNull(found.withdrawalDate)
        assertNull(found.comment)
    }

    @Test
    @Transactional
    fun `create test`() {
        val new = Order(
            name = "someNameCreateTest",
            netSize = Size(100, 100), comment = "commentSome",
            withdrawalDate = LocalDateTime.now(),
            completionDate = LocalDateTime.now(),
            designsNumberForSheet = 4,
            checked = true,
            towerCut = true,
            folding = true,
            clientRepo.findById(1)!!,
            realizationDate = LocalDateTime.now(),
            caretionDate = LocalDateTime.now(),
            3,
            null,
            binderyRepo.findById(1)!!,
            salesmanRepo.findById(1)!!,
            bindingFormRepo.findById(1)!!
        )
        val varn = UVVarnish("addOrderEnobling", null)
        uvVarnishRepo.save(varn)
        new.addOrderEnobling(
            null,
            varn,
            binderyRepo.findByName("A1")!!
        )
        new.addPaperOrderType(
            grammage = 10,
            stockCirculation = 1,
            sheetNumber = 1,
            comment = null,
            circulation = 1,
            platesQuantityForPrinter = 1,
            paperTypeRepo.findById(2)!!,
            printerRepo.findByDigest("DK")!!,
            colouringRepo.save(Colouring(1, 0)),
            impositionTypeRepo.findByName("f/f")!!,
            size = Size(10, 10),
            productionSize = Size(11, 11)
        )
        new.addWorkflowStageStop(
            comment = null,
            lastWorkflowStage = true,
            assignTime = null,
            createTime = LocalDateTime.now(),
            worker = null,
            workflowStage = workflowStageRepo.findById(1)!!
        )
        repo.save(new)
        new.setCalculationCard(
            transport = BigDecimal(10),
            otherCosts = BigDecimal(20),
            enoblingCost = BigDecimal(30),
            bindingCost = BigDecimal(40)
        )
        new.calculationCard?.addPrintCost(
            printCost = BigDecimal(100),
            matrixCost = BigDecimal(200),
            printer = printerRepo.findByDigest("DK")!!
        )
        assertEquals(1, new.paperOrderTypes.size)
        repo.save(new)
        assertNotNull(new.orderid)
        val found = repo.findById(2)!!
        // checking some properties
        assertEquals("someNameCreateTest", found.name)
        assertEquals(100.0, found.netSize.width)
        assertNotNull(found.comment)
        assertEquals("julek@wp.pl", found.client.email?.email)
        assertEquals("A1", found.bindery.name)
        assertEquals("Jan", found.salesman.name)
        assertEquals("Papier", found.bindingForm.name)

        // size of new collections should by 1
        assertEquals(1, found.orderEnoblings.size)
        assertEquals(1, found.paperOrderTypes.size)
        assertEquals(1, found.workflowStageStops.size)
        assertNotNull(found.calculationCard)

        // checking collections
        val orderEnobs = found.orderEnoblings.first()
        assertEquals("addOrderEnobling", orderEnobs.enobling.name)
        assertEquals("A1", orderEnobs.bindery.name)
        val papOTypes = found.paperOrderTypes.first()
        assertEquals(10.toDouble(), papOTypes.grammage)
        assertEquals("Papier Offsetowy", papOTypes.paperType.name)
        assertEquals("duża komori", papOTypes.printer.name)
        assertEquals(1, papOTypes.colouring.firstSide)
        assertEquals("f/f", papOTypes.impositionType.name)
        assertEquals(10.toDouble(), papOTypes.size.width)
        val workflStStop = found.workflowStageStops.first()
        assertNull(workflStStop.comment)
        assertEquals(true, workflStStop.lastWorkflowStage)
        assertEquals("Introligatornia", workflStStop.workflowStage.name)
        // calculation checks
        assertTrue(found.calculationCard?.bindingCost?.compareTo(BigDecimal(40)) == 0)
        assertEquals(1, found.calculationCard?.printCosts?.size)
    }

    @Test
    @Transactional
    fun `association with PaperOrderType test`() {
        val found = repo.findById(1)!!
        assertEquals(2, found.paperOrderTypes.size)
        val papOType = found.paperOrderTypes.find { it.papOrdTypid == 1 }!!
        assertEquals(70.toDouble(), papOType.grammage)
        assertEquals("Papier Offsetowy", papOType.paperType.name)
        assertEquals("duża komori", papOType.printer.name)
        assertEquals(1, papOType.colouring.firstSide)
        assertEquals("f/f", papOType.impositionType.name)
        assertEquals(420.toDouble(), papOType.size.width)
    }

    @Test
    @Transactional
    fun `association with OrderEnobling test`() {
        val found = repo.findById(1)!!
        assertEquals(2, found.orderEnoblings.size)
        val orderEnobs = found.orderEnoblings.first()
        assertEquals("szalony wykrojnik", orderEnobs.enobling.name)
        assertEquals("A2", orderEnobs.bindery.name)
        assertEquals(found.orderid, orderEnobs.order.orderid)
    }

    @Test
    @Transactional
    fun `association with WorkflowStageStop test`() {
        val found = repo.findById(1)!!
        assertEquals(2, found.workflowStageStops.size)
        val workflStStop = found.workflowStageStops.first()
        assertNotNull(workflStStop.comment)
        assertEquals(false, workflStStop.lastWorkflowStage)
        assertEquals("Naświetlarnia", workflStStop.workflowStage.name)
        assertEquals("marianmieszka@wp.pl", workflStStop.worker?.email?.email)
        assertEquals(found.orderid, workflStStop.order.orderid)
    }

    @Test
    @Transactional
    fun `association with CalculationCard test`() {
        val found = repo.findById(1)!!
        assertNotNull(found.calculationCard)
        assertTrue(BigDecimal(1000.00).compareTo(found.calculationCard?.bindingCost) == 0)
        assertTrue(BigDecimal(1245.00).compareTo(found.calculationCard?.enoblingCost) == 0)
        assertTrue(BigDecimal(1251.00).compareTo(found.calculationCard?.otherCosts) == 0)
        assertTrue(BigDecimal(1516.00).compareTo(found.calculationCard?.transport) == 0)
        assertEquals(found.orderid, found.calculationCard?.order?.orderid)
        assertEquals(3, found.calculationCard?.printCosts?.size)
        val printCost = found.calculationCard?.printCosts?.first()!!
        assertEquals("DK", printCost.printer.digest)
        assertTrue(printCost.printCost.compareTo(BigDecimal(12)) == 0)
        assertTrue(printCost.matrixCost.compareTo(BigDecimal(13)) == 0)
    }
}