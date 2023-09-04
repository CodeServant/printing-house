package pl.macia.printinghouse.server.test.repo

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.bmodel.Email
import pl.macia.printinghouse.server.bmodel.IndividualClient
import pl.macia.printinghouse.server.repository.IndividualClientRepo

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
internal class IndividualClientRepoTest {
    @Autowired
    lateinit var repo: IndividualClientRepo

    @Test
    fun `find by id`() {
        var found = repo.findByPersonId(1)
        assertNull(found)
        fun checkFound(found: IndividualClient?) {
            assertEquals("Rick", found?.name)
            assertEquals("SanchesIndividualCli", found?.surname)
            assertEquals("913582395  ", found?.psudoPESEL)
            assertEquals("julek@wp.pl", found?.email?.email)
            assertEquals("984324654", found?.phoneNumber)
        }
        found = repo.findByPersonId(6)
        checkFound(found)
        found = repo.findByClientId(1)
        checkFound(found)
    }

    @Test
    @Transactional
    fun `create new`() {
        val new = IndividualClient(
            email = Email("newEmailFromRepo@example.com"),
            name = "Mario",
            phoneNumber = null,
            psudoPESEL = "52562151525",
            surname = "Bros"
        )
        val test = SingleIdTests<IndividualClient, Int>(repo)
        test.createNew(new, new::personId) {
            repo.findByPersonId(it)
        }
        test.createNew(new, new::clientId) {
            repo.findByClientId(it)
        }
    }
}