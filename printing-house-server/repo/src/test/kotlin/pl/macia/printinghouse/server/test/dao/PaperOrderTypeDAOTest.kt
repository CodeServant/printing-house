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

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class PaperOrderTypeDAOTest {
    @Autowired
    private lateinit var daoColouring: ColouringDAO

    @Autowired
    private lateinit var daoOrder: OrderDAO

    @Autowired
    lateinit var dao: PaperOrderTypeDAO

    @Autowired
    lateinit var daoPaperType: PaperTypeDAO

    @Autowired
    lateinit var daoPrinter: PrinterDAO

    @Autowired
    lateinit var daoImpositionType: ImpositionTypeDAO

    @Autowired
    lateinit var daoSize: SizeDAO

    @Test
    fun `find single by id`() {
        val found = dao.findByIdOrNull(1)!!
        assertEquals("Papier Offsetowy", found.paperType.name)
        assertEquals(70.0, found.grammage, 0.001)
        assertEquals(2, found.colours.firstSide + found.colours.secondSide)
        assertEquals("DK", found.printer.digest)
        assertEquals("f/f", found.imposition.name)
        assertEquals("A2", found.size.name)
        assertEquals(430.0, found.productionSize.width, 0.00001)
    }

    @Test
    @Transactional
    fun `delete one test`() {
        val found = dao.findByIdOrNull(1)!!
        val paperType = found.paperType
        val printer = found.printer
        val imposition = found.imposition
        val size = found.size
        val prodSize = found.productionSize
        val initCount = dao.count()

        dao.delete(found)
        assertEquals(initCount - 1, dao.count())
        assertTrue(daoPaperType.existsById(paperType.id!!))
        assertTrue(daoPrinter.existsById(printer.id!!))
        assertTrue(daoImpositionType.existsById(imposition.id!!))
        assertTrue(daoSize.existsById(size.id!!))
        assertTrue(daoSize.existsById(prodSize.id!!))
    }

    @Test
    @Transactional
    fun `insert one test`() {
        val order = daoOrder.findByIdOrNull(1)!!
        val papType = daoPaperType.findByIdOrNull(1)!!
        val colouring = daoColouring.findByIdOrNull(1)!!
        val printer = daoPrinter.findByDigest("DK")!!
        val impositionType = daoImpositionType.findByName("f/f")!!
        val papOrType = order.addPaperOrderType(
            papType,
            66.0,
            colouring,
            1,
            1,
            1,
            null,
            printer,
            10,
            impositionType,
            daoSize.findByIdOrNull(1)!!,
            daoSize.findByIdOrNull(2)!!
        )
        assertNull(papOrType.id)
        dao.saveAndFlush(papOrType)
        assertNotNull(papOrType.id)
    }
}