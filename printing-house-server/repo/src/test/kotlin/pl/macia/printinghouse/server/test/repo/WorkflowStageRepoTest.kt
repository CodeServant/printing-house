package pl.macia.printinghouse.server.test.repo

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.bmodel.Email
import pl.macia.printinghouse.server.bmodel.Role
import pl.macia.printinghouse.server.bmodel.Worker
import pl.macia.printinghouse.server.bmodel.WorkflowStage
import pl.macia.printinghouse.server.repository.WorkerIntRepo
import pl.macia.printinghouse.server.repository.WorkflowStageIntRepo

private const val simPass = "{bcrypt}\$2a\$12\$onVIlBR/EoHYej8KAvgGYekLQS4/IKVnseD89eYT5YMNjoK3r25W."

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
internal class WorkflowStageRepoTest {
    @Autowired
    lateinit var repo: WorkflowStageIntRepo

    @Autowired
    lateinit var repoWorker: WorkerIntRepo

    @Test
    @Transactional
    fun `find by id test`() {
        val workflowStage = repo.findById(1)!!
        assertEquals("Introligatornia", workflowStage.name)
        assertEquals("introligatornia", workflowStage.role.name)
        assertEquals("Rokita-IntroligManager", workflowStage.workflowManagers.first().surname)
        assertEquals(
            "Introligatornia",
            workflowStage.workflowManagers
                .first().isManagerOf
                .first().name
        )
    }

    @Test
    @Transactional
    fun `create one test`() {
        val nameRl = "newRoleCreateOneWorkflowAStage"
        val ws = WorkflowStage(Role(nameRl), "crtOneWorkflowStage")
        val mgr = Worker(
            Email("createSingleTestWorker@example.com"),
            password = simPass,
            activeAccount = false,
            employed = false,
            name = "John",
            surname = "Travolta",
            pseudoPESEL = "09876543210"
        )
        repoWorker.save(mgr)
        ws.workflowManagers.add(mgr)
        SingleIdTests<WorkflowStage, Int>(repo).createNew(ws, ws::workflowStageid, repo::findById)
    }
}