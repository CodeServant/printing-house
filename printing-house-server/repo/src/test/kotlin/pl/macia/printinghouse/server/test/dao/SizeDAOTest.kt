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
import pl.macia.printinghouse.server.dto.sizeId

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class SizeDAOTest {
    @Autowired
    lateinit var dao: SizeDAO

    @Test
    fun `find by id test`() {
        assertEquals("A0", dao.findByIdOrNull(1)!!.name)
        assertNull(dao.findByIdOrNull(6)!!.name)
    }

    @Test
    @Transactional
    fun `create size`() {
        var saved: Size? = null
        assertDoesNotThrow {
            saved = dao.save(Size(100.0, 100.0))

        }
        assertNotNull(saved?.id)
        assertNull(saved?.name)
        saved = dao.save(Size("A10", 100.0, 101.0))
        assertNotNull(saved?.id)
        assertNotNull(saved?.name)
    }

    @Test
    @Transactional
    fun `teste constraint uniqnes`() {
        dao.save(Size(100.0, 100.0))
        assertThrows<DataIntegrityViolationException> {
            dao.save(Size(100.0, 100.0))
        }
    }

    @Test
    @Transactional
    fun `delete single test`() {
        val idToDelete = 1
        dao.deleteById(idToDelete)
        assertNull(dao.findByIdOrNull(idToDelete), "not properly deleted when $sizeId: $idToDelete")
    }

    @Test
    @Transactional
    fun `findOrCreate test`() {
        assertEquals("A4", dao.findOrCreate(210.0, 297.0).name)
        val nonExistance = dao.findOrCreate(10.0, 17.0)
        assertDoesNotThrow {
            dao.saveAndFlush(nonExistance)
        }
    }
}