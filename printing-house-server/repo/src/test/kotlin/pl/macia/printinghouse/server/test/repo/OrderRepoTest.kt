package pl.macia.printinghouse.server.test.repo

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.repository.OrderRepo

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
internal class OrderRepoTest {
    @Autowired
    lateinit var repo: OrderRepo

    @Test
    fun `find by id test`() {
        val found = repo.findById(1)!!
        assertEquals("gorzelska", found.name)
        assertEquals(205.0, found.netSize.width)
        assertEquals(8, found.pages)
        assertEquals("Jan", found.salesman.name)
        assertEquals("maklowicz@wp.pl", found.client.email?.email)
        assertEquals("Karton", found.bindingForm.name)
        assertEquals("A3", found.bindery.name)
        assertEquals(false, found.folding)
        assertEquals(true, found.towerCut)
        assertTrue(found.url?.url?.isNotEmpty() ?: false)
        assertEquals(
            "draw.io to fajna strona do rysowania diagramów, tego linka można potem odtworzyć i podejrzeć a nawet pozmieniać obrazek",
            found.imageComment
        )
        assertEquals(true, found.checked)
        assertEquals(4, found.designsNumberForSheet)
        assertNull(found.completionDate)
        assertNull(found.withdrawalDate)
        assertNull(found.comment)
    }
}