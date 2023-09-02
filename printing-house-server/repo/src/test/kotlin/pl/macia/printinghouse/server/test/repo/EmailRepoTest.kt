package pl.macia.printinghouse.server.test.repo

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.bmodel.Email
import pl.macia.printinghouse.server.repository.EmailRepo

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
internal class EmailRepoTest {
    @Autowired
    lateinit var dao: EmailRepo

    @Test
    fun `find by id`() {
        assertEquals("anna@wp.pl", dao.findById(1)?.email)
    }

    @Test
    @Transactional
    fun `create new`() {
        val email = Email("newEmailFromRepo@example.com")
        SingleIdTests<Email, Int>(dao).createNew(email, email::emailId, dao::findById)
    }
}