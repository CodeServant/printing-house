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

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class BinderyDAOTest {
    @Autowired
    lateinit var dao: BinderyDAO

    @Order(1)
    @Test
    fun testFindById() {
        val searchedName = "A1"
        val bindery = dao.findById(1)
        assertNotNull(bindery?.name, "can't find $searchedName bindery in the database")
        assertEquals(searchedName, bindery?.name)
        val expectedId = 1
        assertEquals(1, bindery?.id, "expected id is $expectedId")
    }

    @Order(1)
    @Test
    fun testFindByName() {
        val searchedName = "A1"
        val bindery = dao.findByName(searchedName)
        assertNotNull(bindery, "bindery $searchedName not found")
        val expectedId = 1
        assertEquals(1, bindery?.id, "expected id is $expectedId")
    }

    @Test
    fun `test create new`() {
        var bindery = Bindery("testCreateNew")
        dao.create(bindery)
        assertNotNull(bindery.id)
        bindery = Bindery("testCreateNew")
        assertThrows<DataIntegrityViolationException>("data cannot be duplicated") {
            dao.create(bindery)
        }
        val maxLength = 200
        val binderyMetaName = "bindery"
        bindery = Bindery("".padEnd(maxLength, 'a'))
        assertDoesNotThrow("$binderyMetaName name should include $maxLength characters") {
            dao.create(bindery)
        }
        bindery = Bindery("".padEnd(maxLength + 1, 'b'))
        assertThrows<ConstraintViolationException>("$binderyMetaName max length is $maxLength") {
            dao.create(bindery)
        }
        bindery = Bindery(" ")
        assertThrows<ConstraintViolationException> {
            dao.create(bindery)
        }
    }
}