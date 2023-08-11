package pl.macia.printinghouse.server.test.dao

import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.SalesmanDAO
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Order
import pl.macia.printinghouse.server.dao.PersonDAO
import pl.macia.printinghouse.server.dto.Person
import pl.macia.printinghouse.server.dto.Salesman

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class SalesmanDAOTest {
    @Autowired
    lateinit var salesmanDAO: SalesmanDAO

    @Autowired
    lateinit var personDAO: PersonDAO

    @Test
    @Order(1)
    fun `find by id test`() {
        val fetched: Salesman? = salesmanDAO.findByIdOrNull(1)
        assertNotNull(fetched)
        assertEquals("Jan", fetched?.name)
        assertEquals("Kowalski-Salesman", fetched?.surname)
    }

    @Test
    @Order(2)
    fun `equality with person test`() {
        val salesman: Salesman? = salesmanDAO.findByIdOrNull(1)
        val person: Person? = personDAO.findByIdOrNull(1)
        assertEquals(salesman?.pseudoPESEL, person?.pseudoPESEL)
        assertEquals(salesman?.id, person?.id)
    }
}