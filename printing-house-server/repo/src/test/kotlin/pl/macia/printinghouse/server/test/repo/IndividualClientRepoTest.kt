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
import pl.macia.printinghouse.server.repository.ClientIntRepo
import pl.macia.printinghouse.server.repository.IndividualClientIntRepo

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
internal class IndividualClientRepoTest {
    @Autowired
    lateinit var repo: IndividualClientIntRepo

    @Autowired
    lateinit var cliRepo: ClientIntRepo

    @Test
    fun `find by id`() {
        var found = repo.findByPersonId(1)
        assertNull(found)
        fun checkFound(found: IndividualClient?) {
            assertEquals("Rick", found?.name)
            assertEquals("SanchesIndividualCli", found?.surname)
            assertEquals(
                "91357482395",
                found?.psudoPESEL?.trim()
            ) // h2 database does not auto trim fixed char values unlike mysql
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

    @Test
    @Transactional
    fun `test if is individual client`() {
        repo.apply {
            infix fun Boolean.ifIdIs(id:Int) = cliRepo.findById(id)?.isIndividualClient() ?: !this
            true ifIdIs 1
            false ifIdIs 3
        }
    }
}