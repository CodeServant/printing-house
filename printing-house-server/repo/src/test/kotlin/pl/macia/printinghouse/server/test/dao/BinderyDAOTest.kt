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
internal class BinderyDAOTest {
    @Autowired
    lateinit var dao: BinderyDAO

    @Order(1)
    @Test
    fun testFindById() {
        val searchedName = "A1"
        var bindery = dao.findById(1).getOrNull()
        assertNotNull(bindery?.name, "can't find $searchedName ${Bindery.tableBindery} in the database")
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
            assertNotNull(bindery, "${Bindery.tableBindery} $searchedName not found")
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
        assertDoesNotThrow("${Bindery.tableBindery} ${Bindery.binderyName} should include $maxLength characters") {
            dao.saveAndFlush(bindery)
        }
        bindery = Bindery("".padEnd(maxLength + 1, 'b'))
        assertThrows<ConstraintViolationException>("${Bindery.tableBindery} max length is $maxLength") {
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

    @Order(4)
    @Test
    fun `update test`() {
        var bindery = dao.findById(1).getOrNull()
        assertEquals("A1", bindery?.name)
        bindery?.name = "B1"
        dao.save(bindery!!)
        dao.flush()
        bindery = dao.findById(1).getOrNull()
        assertEquals("B1", bindery?.name)
        var binderies = dao.findAllById(listOf(1, 2, 3))
        binderies.forEach {
            it?.name = it?.name?.replace('A', 'B', true)!!
        }
        dao.saveAllAndFlush(binderies)
        binderies = dao.findAllById(listOf(1, 2, 3))
        assertEquals(3, binderies.count { it?.name!!.matches("B.+".toRegex()) })
        binderies.forEach {
            it?.name = it?.name?.replace('B', 'A', true)!!
        }
        binderies = dao.saveAllAndFlush(binderies)
        assertEquals(3, binderies.count { it?.name!!.matches("A.+".toRegex()) })
    }

}


