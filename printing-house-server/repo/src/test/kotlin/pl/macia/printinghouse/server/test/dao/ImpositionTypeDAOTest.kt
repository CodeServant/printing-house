package pl.macia.printinghouse.server.test.dao

import jakarta.transaction.Transactional
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dto.ImpositionType
import pl.macia.printinghouse.server.dao.ImpositionTypeDAO

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ImpositionTypeDAOTest {
    @Autowired
    lateinit var dao: ImpositionTypeDAO

    @Test
    fun `find by id test`() {
        assertEquals(3, dao.count())
        assertEquals("f/f", dao.findByIdOrNull(1)?.name)
    }

    @Test
    @Transactional
    fun `create one test`() {
        val impType = dao.save(ImpositionType("new/new"))
        assertNotNull(impType.id)

    }

    @Test
    @Transactional
    fun `delete one test`() {
        dao.deleteById(2)
        assertEquals(2, dao.count())
    }

    @Test
    fun `foreign key violation test`() {
        assertThrows<DataIntegrityViolationException> {
            dao.deleteById(1)
        }
    }

    @Test
    @Transactional
    fun `name uniqueness check`() {
        assertThrows<DataIntegrityViolationException> {
            dao.save(ImpositionType("f/f"))
        }
    }
}