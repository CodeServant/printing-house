package pl.macia.printinghouse.server.test.repo

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.bmodel.Email
import pl.macia.printinghouse.server.bmodel.Role
import pl.macia.printinghouse.server.bmodel.Salesman
import pl.macia.printinghouse.server.repository.SalesmanIntRepo

private const val smPass = "{bcrypt}\$2a\$12\$onVIlBR/EoHYej8KAvgGYekLQS4/IKVnseD89eYT5YMNjoK3r25W."

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
internal class SalesmanRepoTest {
    @Autowired
    lateinit var repo: SalesmanIntRepo

    @Test
    fun `find by id test`() {
        assertNull(repo.findById(1000))
        val salm = repo.findById(1)!!
        assertEquals("Jan", salm.name)
        assertEquals("Kowalski-Salesman", salm.surname)
        assertEquals("45125478527", salm.psudoPESEL.trim())
        assertEquals("evilcorp@example.com", salm.email.email)
        assertEquals(true, salm.activeAccount)
        assertEquals(true, salm.employed)
        assertEquals(1, salm.roles.size)
        assertEquals("SALESMAN", salm.roles.first().name)
    }

    @Test
    @Transactional
    fun `create new one test`() {
        val new = Salesman(
            Email("newEmailSalesmanRepoTest@example.com"),
            smPass,
            true,
            true,
            "John",
            "Malkovic",
            "01010101010"
        )
        new.roles.add(Role("createdRoleForTest"))
        SingleIdTests<Salesman, Int>(repo).createNew(new, new::personId, repo::findById)
        assertTrue(
            repo.findById(new.personId!!)
                ?.roles
                ?.map { it.name }
                ?.contains("createdRoleForTest") ?: false
        )
    }
}