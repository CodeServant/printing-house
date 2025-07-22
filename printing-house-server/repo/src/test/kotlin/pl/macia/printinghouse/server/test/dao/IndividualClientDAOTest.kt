package pl.macia.printinghouse.server.test.dao

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.ClientDAO
import pl.macia.printinghouse.server.dao.IndividualClientDAO
import pl.macia.printinghouse.server.dto.Client
import pl.macia.printinghouse.server.dto.Email
import pl.macia.printinghouse.server.dto.IndividualClient

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class IndividualClientDAOTest {
    @Autowired
    lateinit var dao: IndividualClientDAO

    @Autowired
    lateinit var daoClient: ClientDAO

    @Test
    @Order(1)
    fun `find by id test`() {
        val indCli = dao.findByIdOrNull(6)!!
        assertEquals(1, indCli.client.id)
    }

    @Test
    @Order(2)
    fun `create new test`() {
        val cli = Client(Email("individualClientTestCreateNew@example.com"), "25825323")
        val indCli = IndividualClient(cli, "TestCreate", "NewIndividualClient", "38541151562")
        dao.saveAndFlush(indCli)
        assertNotNull(cli.id)
        assertNotNull(indCli.id)
    }

    @Test
    fun `find by email test`() {
        val indCli = dao.findByEmail("marian@example.com")!!
        assertEquals("marian@example.com", indCli.client.email?.email)
    }

    @Test
    fun searchQueryTest() {
        val indClients = dao.searchQuery("Rick San")
        assertEquals(1, indClients.size)
        assertEquals("Rick", indClients.first().name)
    }

    @Test
    @Order(3)
    fun `delete one test`() {
        val indCli = dao.findByEmail("individualClientTestCreateNew@example.com")!!
        val cliId = indCli.client.id
        val indCliId = indCli.id!!
        dao.deleteById(indCliId)
        assertNull(dao.findByIdOrNull(indCliId))
        assertNull(daoClient.findByIdOrNull(cliId!!))
        assertNull(dao.findByEmail("thereIsNoSuchEmail@examples.cm"))
    }
}