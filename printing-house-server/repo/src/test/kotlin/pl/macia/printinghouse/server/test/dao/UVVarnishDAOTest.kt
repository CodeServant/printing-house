package pl.macia.printinghouse.server.test.dao

import jakarta.transaction.Transactional
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Example
import org.springframework.data.repository.findByIdOrNull
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.EnoblingDAO
import pl.macia.printinghouse.server.dao.UVVarnishDAO
import pl.macia.printinghouse.server.dto.UVVarnish
import kotlin.jvm.optionals.getOrNull

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class UVVarnishDAOTest {
    @Autowired
    lateinit var dao: UVVarnishDAO

    @Autowired
    lateinit var daoEnobling: EnoblingDAO

    @Test
    @Order(1)
    fun `find by id test`() {
        val uvvarnish = dao.findByIdOrNull(1)!!
        assertEquals("farba kolorowa", uvvarnish.name)
    }

    @Test
    @Order(2)
    fun `create punch test`() {
        val uvvarnish = dao.saveAndFlush(
            UVVarnish("uvvarnishTestCreateOne", null)
        )
        assertNotNull(uvvarnish.id)
        val enobling = daoEnobling.findByIdOrNull(uvvarnish.id)!!
        assertEquals("uvvarnishTestCreateOne", enobling.name)
        assertNull(enobling.description)
    }

    @Test
    @Order(3)
    @Transactional
    fun `name unique test`() {
        assertThrows<DataIntegrityViolationException> {
            dao.saveAndFlush(
                UVVarnish("uvvarnishTestCreateOne", null)
            )
        }
    }

    @Test
    @Order(4)
    fun `delete one test`() {
        val uvvarnishExmpl: Example<UVVarnish> = Example.of(UVVarnish("uvvarnishTestCreateOne", null))
        var findedUVVarnish = dao.findOne(uvvarnishExmpl).getOrNull()
        var findedEnobling = daoEnobling.findOne(uvvarnishExmpl).getOrNull()
        assertNotNull(findedUVVarnish)
        assertNotNull(findedEnobling)
        assertEquals("uvvarnishTestCreateOne", findedUVVarnish?.name)
        assertEquals("uvvarnishTestCreateOne", findedEnobling?.name)
        dao.delete(findedUVVarnish!!)
        findedUVVarnish = dao.findOne(uvvarnishExmpl).getOrNull()
        findedEnobling = daoEnobling.findOne(uvvarnishExmpl).getOrNull()
        assertNull(findedUVVarnish)
        assertNull(findedEnobling)
    }
}