package pl.macia.printinghouse.server.test.repo

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.bmodel.Colouring
import pl.macia.printinghouse.server.repository.ColouringIntRepo

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
internal class ColouringRepoTest {
    @Autowired
    lateinit var repo: ColouringIntRepo

    @Test
    fun `find by id test`() {
        val found = repo.findById(1)!!
        assertEquals(4, found.firstSide)
        assertEquals(0, found.secondSide)
    }

    @Test
    @Transactional
    fun `create new`() {
        val new = Colouring(1, 0)
        SingleIdTests<Colouring, Byte>(repo).createNew(new, new::colouringId, repo::findById)
    }

    @Test
    @Transactional
    fun `find by palette test`() {
        val found = repo.findByPalette(2, 0)!!
        assertEquals(2, found.colouringId)
        val notFound = repo.findByPalette(1, 0)
        assertNull(notFound)
    }
}