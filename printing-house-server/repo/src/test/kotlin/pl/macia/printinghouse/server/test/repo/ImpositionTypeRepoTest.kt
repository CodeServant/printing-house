package pl.macia.printinghouse.server.test.repo

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.bmodel.ImpositionType
import pl.macia.printinghouse.server.repository.ImpositionTypeIntRepo

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
internal class ImpositionTypeRepoTest {
    @Autowired
    lateinit var repo: ImpositionTypeIntRepo

    @Test
    fun `find by id`() {
        var found = repo.findById(1)
        assertEquals("f/f", found?.name)
        found = repo.findById(2)
        assertEquals("f/o", found?.name)
        found = repo.findById(1000)
        assertNull(found)
    }

    @Test
    fun `create new`() {
        val new = ImpositionType("s/s")
        SingleIdTests<ImpositionType, Int>(repo).createNew(new, new::impTypId, repo::findById)
    }

    @Test
    @Transactional
    fun `find by name test`() {
        val found = repo.findByName("f/f")
        assertEquals(1, found?.impTypId)
        val notFound = repo.findByName("nonExistent")
        assertNull(notFound)
    }
}