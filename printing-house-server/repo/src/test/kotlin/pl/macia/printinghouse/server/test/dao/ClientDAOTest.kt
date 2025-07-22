package pl.macia.printinghouse.server.test.dao

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.ClientDAO
import pl.macia.printinghouse.server.dto.Client
import pl.macia.printinghouse.server.dto.Email

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class ClientDAOTest {
    @Autowired
    lateinit var dao: ClientDAO

    @Test
    fun `find by id test`() {
        val client = dao.findByIdOrNull(1)!!
        assertEquals("984324654", client.phoneNumber)
    }

    @Test
    fun `email association test`() {
        val client = dao.findByIdOrNull(1)!!
        assertEquals("marian@example.com",client.email?.email)
    }

    @Test
    fun `insert new test`() {
        var client = Client(Email("insertNewTestClientDAO@gmail.com"), null)
        dao.saveAndFlush(client)
        client = dao.findByIdOrNull(client.id!!)!!
        assertEquals("insertNewTestClientDAO@gmail.com", client.email?.email)
        assertNull(client.phoneNumber)
    }
}