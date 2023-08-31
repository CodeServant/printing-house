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
import pl.macia.printinghouse.server.dao.PaperTypeDAO
import pl.macia.printinghouse.server.dto.PaperType

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class PaperTypeDAOTest {
    @Autowired
    lateinit var dao: PaperTypeDAO

    @Test
    fun `find by id test`() {
        val found = dao.findByIdOrNull(1)!!
        assertEquals("Papier Błysk", found.name)
        assertEquals("Pap Błysk", found.shortName)
    }

    @Test
    @Transactional
    fun `create test`() {
        val inserted = dao.save(PaperType("New Paper", "newP"))
        assertNotNull(inserted.id)
        assertEquals("newP", inserted.shortName)
    }

    @Test
    @Transactional
    fun `short name validation test`() {
        dao.save(PaperType("New Paper", "newP"))
        assertThrows<DataIntegrityViolationException> {
            dao.save(PaperType("New Paper2", "newP"))
        }
    }

    @Test
    @Transactional
    fun `name validation test`() {
        dao.save(PaperType("New Paper", "newP"))
        assertThrows<DataIntegrityViolationException> {
            dao.save(PaperType("New Paper", "newP2"))
        }
    }

    @Test
    @Transactional
    fun `delete on test`() {
        val found = dao.findByIdOrNull(1)!!
        dao.delete(found)
        assertFalse(dao.findAll().contains(found))
    }

    @Test
    fun `foreign key constraint test`() {
        assertThrows<DataIntegrityViolationException> {
            dao.deleteById(2)
        }
    }
}