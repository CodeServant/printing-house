package pl.macia.printinghouse.server.test.repo

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.bmodel.Email
import pl.macia.printinghouse.server.bmodel.Worker
import pl.macia.printinghouse.server.repository.WorkerIntRepo

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
internal class WorkerRepoTest {
    @Autowired
    lateinit var repo: WorkerIntRepo

    @Test
    @Transactional
    fun `find by id test`() {
        var found = repo.findById(2)!!
        assertEquals(found.name, "Marian")
        assertEquals(found.surname, "Rokita-IntroligManager")
        assertEquals(found.psudoPESEL.trim(), "548746687")
        assertTrue(found.isManagerOf.isNotEmpty())
        assertEquals(found.isManagerOf.first().name, "Introligatornia")
    }

    @Test
    @Transactional
    fun `create single test`() {
        val new = Worker(
            Email("createSingleTestWorker@example.com"),
            password = "{bcrypt}\$2a\$12\$onVIlBR/EoHYej8KAvgGYekLQS4/IKVnseD89eYT5YMNjoK3r25W.",
            activeAccount = false,
            employed = false,
            name = "John",
            surname = "Travolta",
            pseudoPESEL = "09876543210"
        )
        SingleIdTests<Worker, Int>(repo).createNew(new, new::personId, repo::findById)
    }

    @Test
    fun `Worker and WorkflowStage binding test`() {
        TODO("Not yet implemented")
    }
}