package pl.macia.printinghouse.server.test.dao

import jakarta.transaction.Transactional
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Example
import org.springframework.data.repository.findByIdOrNull
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.EnoblingDAO
import pl.macia.printinghouse.server.dao.PunchDAO
import pl.macia.printinghouse.server.dto.Punch
import kotlin.jvm.optionals.getOrNull

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class PunchDAOTest {
    @Autowired
    lateinit var dao: PunchDAO

    @Autowired
    lateinit var daoEnobling: EnoblingDAO

    @Test
    @Order(1)
    fun `find by id test`() {
        val punch = dao.findByIdOrNull(4)!!
        assertEquals("szalony wykrojnik", punch.name)
    }

    @Test
    @Order(2)
    fun `create punch test`() {
        val punch = dao.saveAndFlush(
            Punch("punchTestCreateOne", null)
        )
        assertNotNull(punch.id)
        val enobling = daoEnobling.findByIdOrNull(punch.id!!)!!
        assertEquals("punchTestCreateOne", enobling.name)
        assertNull(enobling.description)
    }

    @Test
    @Order(3)
    @Transactional
    fun `name unique test`() {
        assertThrows<DataIntegrityViolationException> {
            dao.saveAndFlush(
                Punch("punchTestCreateOne", null)
            )
        }
    }

    @Test
    @Order(4)
    fun `delete one test`() {
        val punchExmpl: Example<Punch> = Example.of(Punch("punchTestCreateOne", null))
        var findedPunch = dao.findOne(punchExmpl).getOrNull()
        var findedEnobling = daoEnobling.findOne(punchExmpl).getOrNull()
        assertNotNull(findedPunch)
        assertNotNull(findedEnobling)
        assertEquals("punchTestCreateOne", findedPunch?.name)
        assertEquals("punchTestCreateOne", findedEnobling?.name)
        dao.delete(findedPunch!!)
        findedPunch = dao.findOne(punchExmpl).getOrNull()
        findedEnobling = daoEnobling.findOne(punchExmpl).getOrNull()
        assertNull(findedPunch)
        assertNull(findedEnobling)
    }
}