package pl.macia.printinghouse.server.test.dao

import jakarta.transaction.Transactional
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.SizeDAO
import pl.macia.printinghouse.server.dto.Size

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class SizeDAOTest {
    @Autowired
    lateinit var dao: SizeDAO

    @Test
    fun `find by id test`() {
        assertEquals("A0", dao.findByIdOrNull(1)!!.name)
        assertNull(dao.findByIdOrNull(6)?.name)
    }

    @Test
    @Transactional
    fun `create size`() {
        var saved = Size("A10", 100.0, 101.0)
        saved = dao.save(saved)
        assertNotNull(saved.id)
        assertNotNull(saved.name)
    }

    @Test
    @Transactional
    fun `teste constraint uniqnes`() {
        val new = dao.save(Size("NEW", 100.0, 100.0))
        assertThrows<DataIntegrityViolationException> {
            dao.save(Size("OTHER", 100.0, 100.0))
        }
        dao.delete(new)
    }

    @Test
    @Transactional
    fun `delete single test`() {
        val idToDelete = 1
        dao.deleteById(idToDelete)
        assertNull(dao.findByIdOrNull(idToDelete), "not properly deleted when ${Size.ID}: $idToDelete")
    }

    @Test
    fun `findByNameInNotNull test`() {
        val foundList = dao.findByNameIsNotNull()
        val initialSize = foundList.size
        val filterefSize = foundList.filter {
            it.name != null
        }.size
        assertEquals(initialSize, filterefSize)
    }
}