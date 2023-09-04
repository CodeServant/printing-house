package pl.macia.printinghouse.server.test.repo

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.bmodel.UVVarnish
import pl.macia.printinghouse.server.repository.UVVarnishIntRepo

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
internal class UVVarnishRepoTest {
    @Autowired
    lateinit var repo: UVVarnishIntRepo

    @Test
    fun `find by id test`() {
        val found = repo.findById(1)!!
        assertEquals("farba kolorowa", found.name)
        assertEquals("to jest farba kolorowa", found.description)
    }

    @Test
    @Transactional
    fun `create new`() {
        val new = UVVarnish("newUVVarnishTestPunchRepoTest")
        SingleIdTests<UVVarnish, Int>(repo).createNew(new, new::enoblingId, repo::findById)
    }
}