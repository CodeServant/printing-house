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
import pl.macia.printinghouse.server.dto.binderyName
import pl.macia.printinghouse.server.dto.tableBindery
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
        var bindery = dao.findById(1).getOrNull()
        assertNotNull(bindery?.name, "can't find $searchedName $tableBindery in the database")
        assertEquals(searchedName, bindery?.name)
        val expectedId = 1
        assertEquals(1, bindery?.id, "expected id is $expectedId")
        val nonExistingId = 99999
        assertDoesNotThrow {
            bindery = dao.findById(nonExistingId).getOrNull()
        }
        assertNull(bindery)
    }

    @Order(1)
    @Test
    fun testFindByName() {
        fun bulkTests(searchedName: String, expectedId: Int) {
            val bindery = dao.findByName(searchedName)
            assertNotNull(bindery, "$tableBindery $searchedName not found")
            assertEquals(expectedId, bindery?.id, "expected id is $expectedId")
        }
        var bind: Bindery? = null
        assertDoesNotThrow {
            bind = dao.findByName("does not exists in database")
        }
        assertNull(bind)
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
        bindery = Bindery("".padEnd(maxLength, 'a'))
        assertDoesNotThrow("$tableBindery $binderyName should include $maxLength characters") {
            dao.saveAndFlush(bindery)
        }
        bindery = Bindery("".padEnd(maxLength + 1, 'b'))
        assertThrows<ConstraintViolationException>("$tableBindery max length is $maxLength") {
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
        val binToDel = "testCreateNew"
        var bindery = dao.findByName(binToDel)

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