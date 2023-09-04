package pl.macia.printinghouse.server.test.dao

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.CompanyDAO
import pl.macia.printinghouse.server.dto.Client
import pl.macia.printinghouse.server.dto.Company
import pl.macia.printinghouse.server.dto.Email

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class CompanyDAOTest {
    @Autowired
    lateinit var dao: CompanyDAO

    @Test
    @Order(1)
    fun `find by id test`() {
        var corp = dao.findByIdOrNull(1)!!
        assertEquals("evil corp inc.", corp.name)
        corp = dao.findClientById(3)!!
        assertEquals("evil corp inc.", corp.name)
    }

    @Test
    @Order(2)
    fun `client association test`() {
        val corpClient = dao.findByIdOrNull(1)!!.client
        assertEquals(3, corpClient?.id)
    }

    @Test
    @Order(3)
    fun `insertion test`() {
        val client = Client(Email("insertionCompanyClientTest@example.com"), "945036196")
        val company = Company("InsertionNewCompanyName inc.", "0000000000", client)
        assertDoesNotThrow {
            dao.saveAndFlush(company)
        }
        assertNotNull(company)
        assertNotNull(client)
    }
}