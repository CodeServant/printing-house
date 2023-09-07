package pl.macia.printinghouse.server.test.repo

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.bmodel.PaperType
import pl.macia.printinghouse.server.repository.PaperTypeIntRepo

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
internal class PaperTypeRepoTest {
    @Autowired
    lateinit var repo: PaperTypeIntRepo

    @Test
    fun `find by id test`() {
        var found = repo.findById(1)
        assertEquals("Papier Błysk", found?.name)
        assertEquals("Pap Błysk", found?.shortName)
        found = repo.findById(2)
        assertEquals("Papier Offsetowy", found?.name)
        assertEquals("Offsetowy", found?.shortName)
        found = repo.findById(1000)
        assertNull(found)
    }

    @Test
    fun `create test`() {
        var new = PaperType("new paper create test repo", "npctr")
        SingleIdTests<PaperType, Int>(repo).createNew(new, new::papTypeId, repo::findById)
    }
}