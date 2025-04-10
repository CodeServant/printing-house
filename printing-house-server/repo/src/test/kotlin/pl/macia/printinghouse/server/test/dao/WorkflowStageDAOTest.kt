package pl.macia.printinghouse.server.test.dao

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Order as JpaOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.WorkerDAO
import pl.macia.printinghouse.server.dao.WorkflowStageDAO
import pl.macia.printinghouse.server.dto.*
import kotlin.jvm.optionals.getOrNull

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class WorkflowStageDAOTest {
    @Autowired
    lateinit var dao: WorkflowStageDAO

    @Autowired
    lateinit var daoWorker: WorkerDAO

    @JpaOrder(1)
    @Test
    fun `get by id`() {
        val workflowStage = dao.findByIdOrNull(2)
        assertEquals("Introligatornia", workflowStage?.name)
    }

    @Test
    @JpaOrder(3)
    fun `data violation test`() {
        val workflowStage = dao.findByIdOrNull(2)
        assertThrows<DataIntegrityViolationException> {
            dao.delete(workflowStage!!)
        }
    }

    @JpaOrder(4)
    @Test
    @Transactional
    fun `delete workflowstage that has role association`() {
        val workerManager = daoWorker.findByIdOrNull(4)!! // Makłowisz-NaśwManager
        val workflowStage =
            WorkflowStage("delete role that has association", mutableListOf(workerManager))
        dao.saveAndFlush(workflowStage)
        assertNotNull(workflowStage.id)
        assertDoesNotThrow {
            dao.delete(workflowStage)
        }
    }

    @Test
    @JpaOrder(5)
    @Transactional
    fun `worker association test`() {
        val workflowStage = dao.findByIdOrNull(3)!!
        assertEquals(
            4, workflowStage
                .workflowStageManagers
                .stream()
                .findFirst().getOrNull()
                ?.id
        )
    }

    @Test
    @JpaOrder(6)
    @Transactional
    fun `create new worker assoc`() {
        val workflowStage = dao.findByIdOrNull(2)!!
        val newWorker = Worker(
            email = Email("createNewWorkerAssocTest@example.com"),
            name = "Jan",
            surname = "Kowalski",
            password = "{bcrypt}\$2a\$12\$onVIlBR/EoHYej8KAvgGYekLQS4/IKVnseD89eYT5YMNjoK3r25W.",
            activeAccount = true,
            employed = true,
            pseudoPESEL = "11111111111"
        )
        // you need to first save them to database
        dao.save(workflowStage)
        daoWorker.save(newWorker)
        // then add to each other
        workflowStage.workflowStageManagers.add(newWorker)
        newWorker.isManagerOf.add(workflowStage)
        assertNotNull(newWorker.id)
        assertEquals("Jan", daoWorker.findByIdOrNull(newWorker.id)?.name)
        assertTrue(dao.findByIdOrNull(2)?.workflowStageManagers?.contains(newWorker)!!)
    }
}