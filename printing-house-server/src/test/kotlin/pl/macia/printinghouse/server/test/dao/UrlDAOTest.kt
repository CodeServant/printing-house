package pl.macia.printinghouse.server.test.dao

import jakarta.transaction.Transactional
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.UrlDAO
import pl.macia.printinghouse.server.dto.URL

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class UrlDAOTest {
    @Autowired
    lateinit var dao: UrlDAO

    @Test
    fun `find by id test`() {
        val found = dao.findByIdOrNull(1)!!
        assertTrue(
            found.url.contains("FRwubm%2F4Bx8oOWNu%2BFZDzU%2F0cLZy8CvFpTnvLtqiKHVejVH18EY569podYRECWV9U6mkc3nnQtxI%2F6FMmETzHz6Il2Uify")
        )
    }

    @Test
    @Transactional
    fun `insert one`() {
        assertDoesNotThrow {
            val inserted = dao.save(URL("https://www.wikipedia.org"))
            assertNotNull(inserted.id)
        }
    }

    @Test
    @Transactional
    fun `delete one test`() {
        assertEquals(1, dao.count())
        val id = dao.save(URL("https://wikipedia.org")).id!!
        assertEquals(2, dao.count())
        dao.deleteById(id)
        assertEquals(1, dao.count())

    }

    @Test
    fun `foreign key constraint test`() {
        assertThrows<DataIntegrityViolationException> {
            dao.deleteById(1)
        }
    }
}