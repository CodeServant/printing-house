package pl.macia.printinghouse.server.test.repo

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.bmodel.Punch
import pl.macia.printinghouse.server.repository.PunchIntRepo

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
internal class PunchRepoTest {
    @Autowired
    lateinit var repo: PunchIntRepo

    @Test
    fun `find by id test`() {
        val found = repo.findById(4)!!
        assertEquals("szalony wykrojnik", found.name)
        assertNull(found.description)
    }

    @Test
    @Transactional
    fun `create new`() {
        val new = Punch("newPunchTestPunchRepoTest")
        SingleIdTests<Punch, Int>(repo).createNew(new, new::enoblingId, repo::findById)
    }
}