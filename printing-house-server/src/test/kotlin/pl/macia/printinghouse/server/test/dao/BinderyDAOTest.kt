package pl.macia.printinghouse.server.test.dao

import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.BinderyDAO
import pl.macia.printinghouse.server.dto.Bindery
import java.lang.NullPointerException
import kotlin.jvm.optionals.getOrNull

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class BinderyDAOTest {
    @Autowired
    lateinit var dao: BinderyDAO

    @Order(1)
    @Test
    fun testFindById() {
        val searchedName = "A1"
        val bindery = dao.findById(1).getOrNull()
        assertNotNull(bindery?.name, "can't find $searchedName bindery in the database")
        assertEquals(searchedName, bindery?.name)
        val expectedId = 1
        assertEquals(1, bindery?.id, "expected id is $expectedId")
    }

    @Order(1)
    @Test
    fun testFindByName() {
        fun bulkTests(searchedName: String, expectedId: Int) {
            val bindery = dao.findByName(searchedName)
            assertNotNull(bindery, "bindery $searchedName not found")
            assertEquals(expectedId, bindery?.id, "expected id is $expectedId")
        }
        bulkTests("A1", 1)
        bulkTests("A2", 2)
    }

    @Order(2)
    @Test
    fun `test create new`() {
        var bindery = Bindery("testCreateNew")
        dao.saveAndFlush(bindery)
        assertNotNull(bindery.id)
        bindery = Bindery("testCreateNew")
        assertThrows<DataIntegrityViolationException>("data cannot be duplicated") {
            dao.saveAndFlush(bindery)
        }
        val maxLength = 200
        val binderyMetaName = "bindery"
        bindery = Bindery("".padEnd(maxLength, 'a'))
        assertDoesNotThrow("$binderyMetaName name should include $maxLength characters") {
            dao.saveAndFlush(bindery)
        }
        bindery = Bindery("".padEnd(maxLength + 1, 'b'))
        assertThrows<ConstraintViolationException>("$binderyMetaName max length is $maxLength") {
            dao.saveAndFlush(bindery)
        }
        bindery = Bindery(" ")
        assertThrows<ConstraintViolationException> {
            dao.saveAndFlush(bindery)
        }
    }

    @Order(3)
    @Test
    fun `test delete single`() {
        var bindery: Bindery? = null
        val binToDel = "testCreateNew"

        bindery = dao.findByName(binToDel)

        assertNotNull(bindery)

        assertDoesNotThrow {
            dao.delete(bindery!!)

        }
        assertDoesNotThrow {
            dao.delete(bindery!!)
        }
        assertDoesNotThrow {
            bindery = dao.findByName(binToDel)
        }
        assertNull(bindery)
        assertThrows<NullPointerException> {
            dao.delete(bindery!!)
        }
        assertDoesNotThrow {
            dao.delete(Bindery("should Give Error"))
        }
    }
}