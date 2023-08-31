package pl.macia.printinghouse.server.test.dao

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.EnoblingDAO

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class EnoblingDAOTest {
    @Autowired
    lateinit var dao: EnoblingDAO

    @Test
    @Order(1)
    fun `find by id test`() {
        var enobling = dao.findByIdOrNull(1)
        assertNotNull(enobling)
        assertEquals("farba kolorowa", enobling?.name)
        assertNotNull(enobling?.description)
        enobling = dao.findByIdOrNull(2)
        assertNotNull(enobling?.name)
        assertNull(enobling?.description)
    }

    @Test
    @Order(2)
    fun `delete constraints test`() {
        assertThrows<DataIntegrityViolationException> {
            dao.deleteById(1)
        }
    }
}