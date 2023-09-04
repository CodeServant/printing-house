package pl.macia.printinghouse.server.test.repo

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.bmodel.CompanyClient
import pl.macia.printinghouse.server.bmodel.Email
import pl.macia.printinghouse.server.repository.CompanyClientIntRepo

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
internal class CompanyClientRepoTest {
    @Autowired
    lateinit var repo: CompanyClientIntRepo

    @Test
    fun `find by id test`() {
        var found = repo.findByClientId(3)!!
        val comName = "evil corp inc."
        assertEquals(comName, found.name)
        found = repo.findByCompanyId(1)!!
        assertEquals(comName, found.name)
        assertNull(repo.findByClientId(1))
    }

    @Test
    @Transactional
    fun `create new`() {
        val new = CompanyClient("example corp.", "9182736456", Email("exampleEmailCompClinew@example.com"), "000000000")
        SingleIdTests<CompanyClient, Int>(repo).createNew(new, new::companyId, repo::findByCompanyId)
        SingleIdTests<CompanyClient, Int>(repo).createNew(new, new::clientId, repo::findByClientId)
    }
}