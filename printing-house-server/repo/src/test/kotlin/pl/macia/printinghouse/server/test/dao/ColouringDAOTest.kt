package pl.macia.printinghouse.server.test.dao

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.ColouringDAO
import pl.macia.printinghouse.server.dto.Colouring

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class ColouringDAOTest {
    @Autowired
    lateinit var dao: ColouringDAO

    @Test
    fun `find by id`() {
        assertEquals(0, dao.findByIdOrNull(2)?.secondSide)
        assertNull(dao.findByIdOrNull(10)?.secondSide)
    }

    @Test
    @Transactional
    fun `create one test`() {
        var inserted: Colouring? = null
        assertDoesNotThrow {
            inserted = dao.save(Colouring(3, 2))
        }
        assertNotNull(inserted?.id)
        assertEquals(3, inserted?.firstSide)
    }

    @Test
    fun `unique validation test`() {
        assertThrows<DataIntegrityViolationException> {
            dao.save(Colouring(1, 1))
        }
    }

    @Test
    fun `foreign key constraint test`() {
        assertThrows<DataIntegrityViolationException> {
            dao.deleteById(4)
        }
    }

    @Test
    @Transactional
    fun `delete one test`() {
        dao.deleteById(1)
        assertEquals(3, dao.count())
    }

    @Test
    @Transactional
    fun `first side higher test`() {
        assertThrows<DataIntegrityViolationException> {
            dao.save(Colouring(0,4))
        }
    }
}