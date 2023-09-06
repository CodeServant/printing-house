package pl.macia.printinghouse.server.test.repo

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.repository.ClientIntRepo

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
internal class ClientRepoTest {
    @Autowired
    lateinit var repo: ClientIntRepo

    @Test
    fun `find by id`() {
        var found = repo.findById(1)
        assertNotNull(found)
        assertEquals("julek@wp.pl", found?.email?.email)
        assertEquals("984324654", found?.phoneNumber)
        found = repo.findById(3)
        assertEquals("152358752", found?.phoneNumber)
    }
}