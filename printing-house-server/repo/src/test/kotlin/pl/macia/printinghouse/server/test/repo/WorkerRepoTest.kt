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
import pl.macia.printinghouse.server.bmodel.WorkflowStage
import pl.macia.printinghouse.server.repository.WorkerIntRepo
import pl.macia.printinghouse.server.repository.WorkflowStageIntRepo

private const val easyPass = "{bcrypt}\$2a\$12\$onVIlBR/EoHYej8KAvgGYekLQS4/IKVnseD89eYT5YMNjoK3r25W."

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
internal class WorkerRepoTest {
    @Autowired
    lateinit var repo: WorkerIntRepo

    @Autowired
    lateinit var repoWorkflowStage: WorkflowStageIntRepo

    @Test
    @Transactional
    fun `find by id test`() {
        val found = repo.findById(2)!!
        assertEquals(found.name, "Marian")
        assertEquals(found.surname, "Rokita-IntroligManager")
        assertEquals("54874668723", found.psudoPESEL.trim())
        assertTrue(found.isManagerOf.isNotEmpty())
        assertEquals(found.isManagerOf.first().name, "Introligatornia")
        assertNotNull(found.roles.find { it.name == "krajalnia" })
    }

    @Test
    @Transactional
    fun `create single test`() {
        val new = Worker(
            Email("createSingleTestWorker@example.com"),
            password = easyPass,
            activeAccount = false,
            employed = false,
            name = "John",
            surname = "Travolta",
            pseudoPESEL = "09876543210"
        )
        SingleIdTests<Worker, Int>(repo).createNew(new, new::personId, repo::findById)
    }

    @Test
    @Transactional
    fun `Worker and WorkflowStage binding test`() {
        val new = Worker(
            Email("createSingleTestWorker@example.com"),
            password = easyPass,
            activeAccount = false,
            employed = false,
            name = "John",
            surname = "Travolta",
            pseudoPESEL = "09876543210"
        )
        val ws1 = WorkflowStage(name = "Station1")
        val ws2 = WorkflowStage(name = "Station2")
        val listOfWss = listOf(ws1, ws2)

        listOfWss.forEach {
            it.workflowManagers.add(new)
        }
        new.isManagerOf.addAll(listOfWss)
        listOfWss.forEach(repoWorkflowStage::save)
        repo.save(new)
        assertNotNull(ws1.workflowStageid)
        assertNotNull(ws2.workflowStageid)
        assertNotNull(new.personId)
        repo.findById(new.personId!!)
            ?.isManagerOf
            ?.let { manager ->
                assertTrue(manager.containsAll(listOfWss))
            }
    }
}