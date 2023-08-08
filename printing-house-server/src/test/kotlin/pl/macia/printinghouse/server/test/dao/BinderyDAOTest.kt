package pl.macia.printinghouse.server.test.dao

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.BinderyDAO

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
        assertNotEquals(null, bindery?.name, "can't find $searchedName bindery in the database")
        assertEquals(searchedName, bindery?.name)
        val expectedId = 1
        assertEquals(1, bindery?.id, "expected id is $expectedId")
    }

    @Order(1)
    @Test
    fun testFindByName() {
        val searchedName = "A1"
        val bindery = dao.findByName(searchedName)
        assertNotEquals(null, bindery, "bindery $searchedName not found")
        val expectedId = 1
        assertEquals(1, bindery?.id, "expected id is $expectedId")
    }
}