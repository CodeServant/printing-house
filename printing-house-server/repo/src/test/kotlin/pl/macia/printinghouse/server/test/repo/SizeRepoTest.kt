package pl.macia.printinghouse.server.test.repo

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.bmodel.Size
import pl.macia.printinghouse.server.repository.SizeIntRepo

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
internal class SizeRepoTest {
    @Autowired
    lateinit var repo: SizeIntRepo

    @Test
    fun `find by id test`() {
        var found = repo.findById(1)
        assertEquals("A0", found?.name)
        found = repo.findById(6)
        assertNull(found?.name)
        assertEquals(123.0, found?.width)
        found = repo.findById(1000)
        assertNull(found)
    }

    @Test
    @Transactional
    fun `create new test`() {
        val new = Size("repotest", 134.0, 151.0)
        SingleIdTests<Size, Int>(repo).createNew(new, new::sizeId, repo::findById)
    }
}