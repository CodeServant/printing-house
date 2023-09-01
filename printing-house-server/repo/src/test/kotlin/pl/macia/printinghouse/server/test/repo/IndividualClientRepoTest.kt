package pl.macia.printinghouse.server.test.repo

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.repository.IndividualClientRepo

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
class IndividualClientRepoTest {
    @Autowired
    lateinit var repo: IndividualClientRepo

    @Test
    fun findById() {
        var found = repo.findById(1)
        assertNull(found)
        found = repo.findById(6)
        assertEquals("Rick", found?.name)
        assertEquals("SanchesIndividualCli", found?.surname)
        assertEquals("913582395  ", found?.psudoPESEL)
        assertEquals("julek@wp.pl", found?.email?.email)
        assertEquals("984324654", found?.phoneNumber)
    }
}