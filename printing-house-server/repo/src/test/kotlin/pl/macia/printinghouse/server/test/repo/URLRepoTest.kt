package pl.macia.printinghouse.server.test.repo

import jakarta.transaction.Transactional
import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.bmodel.URL
import pl.macia.printinghouse.server.repository.URLIntRepo
import org.junit.jupiter.api.*

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
internal class URLRepoTest {
    @Autowired
    lateinit var repo: URLIntRepo

    @Test
    fun `find by id test`() {
        var found = repo.findById(1)
        assertNotNull(found)
        found = repo.findById(100)
        assertNull(found)
    }

    @Test
    @Transactional
    fun `create new`() {
        val new = URL("https://example.com")
        SingleIdTests<URL, Long>(repo).createNew(new, new::urlId, repo::findById)
    }

    @Test
    @Transactional
    fun `constraint test`() {
        val new = URL("https")
        assertThrows<ConstraintViolationException> {
            SingleIdTests<URL, Long>(repo).createNew(new, new::urlId, repo::findById)
        }
    }
}