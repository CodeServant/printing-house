package pl.macia.printinghouse.server.test.repo

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.bmodel.Enobling
import pl.macia.printinghouse.server.repository.EnoblingIntRepo

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
internal class EnoblingRepoTest {
    @Autowired
    lateinit var repo: EnoblingIntRepo

    @Test
    fun `find by id test`() {
        val found = repo.findById(1)!!
        assertEquals("farba kolorowa", found.name)
    }

    @Test
    @Transactional
    fun `create new`() {
        val new = Enobling("EnoblingRepoTestCreateNew")
        SingleIdTests<Enobling,Int>(repo).createNew(new, new::enoblingId, repo::findById)
    }
}