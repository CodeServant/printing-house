package pl.macia.printinghouse.server.test.repo

import jakarta.transaction.Transactional
import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.bmodel.Image
import pl.macia.printinghouse.server.repository.ImageIntRepo
import org.junit.jupiter.api.*

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
internal class ImageRepoTest {
    @Autowired
    lateinit var repo: ImageIntRepo

    @Test
    @Transactional
    fun `find by id test`() {
        var found = repo.findById(1)
        assertNotNull(found)
        assertEquals(
            "draw.io to fajna strona do rysowania diagramów, tego linka można potem odtworzyć i podejrzeć a nawet pozmieniać obrazek",
            found?.imageComment
        )
        found?.imageComment = "changed comment"
        found = repo.findById(1)
        assertEquals("changed comment", found?.imageComment)
        found = repo.findById(100)
        assertNull(found)
    }

    @Test
    @Transactional
    fun `create new`() {
        val new = Image("https://example.com", "some comment")
        SingleIdTests<Image, Long>(repo).createNew(new, new::impImgId, repo::findById)
    }

    @Test
    @Transactional
    fun `constraint test`() {
        val new = Image("https", null)
        assertThrows<ConstraintViolationException> {
            SingleIdTests<Image, Long>(repo).createNew(new, new::impImgId, repo::findById)
        }
    }
}