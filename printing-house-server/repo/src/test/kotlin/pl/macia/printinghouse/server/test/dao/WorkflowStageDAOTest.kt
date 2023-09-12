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
import pl.macia.printinghouse.server.dao.RoleDAO
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
    lateinit var daoRole: RoleDAO

    @Autowired
    lateinit var daoWorker: WorkerDAO

    @JpaOrder(1)
    @Test
    fun `get by id`() {
        val workflowStage = dao.findByIdOrNull(1)
        assertEquals("Introligatornia", workflowStage?.name)
    }

    @JpaOrder(2)
    @Test
    @Transactional
    fun `role association test`() {
        val workerManager = daoWorker.findByIdOrNull(4)!! // Makłowisz-NaśwManager
        val newWorkflowStage =
            WorkflowStage(
                Role("newRoleToWorkflowStage role association test"),
                "introligatorNewWorkflowStage test",
                mutableSetOf(workerManager)
            )
        dao.saveAndFlush(newWorkflowStage)
        assertNotNull(newWorkflowStage.id, "${WorkflowStage.TABLE_NAME} not created")
        assertNotNull(newWorkflowStage.role.id, "${Role.TABLE_NAME} not created")
        newWorkflowStage.role = Role("newRoleToWorkflowStage")
        dao.saveAndFlush(newWorkflowStage)
        assertNotNull(newWorkflowStage.role.id)
        val deletedId = newWorkflowStage.role.id!!
        dao.delete(newWorkflowStage)
        assertNull(dao.findByIdOrNull(deletedId))
    }

    @JpaOrder(3)
    @Test
    fun `data integrity test`() {
        var workflowStage = dao.findByIdOrNull(1)
        assertThrows<DataIntegrityViolationException> {
            dao.delete(workflowStage!!)
        }
        workflowStage = dao.findByIdOrNull(3)
        assertDoesNotThrow {
            dao.delete(workflowStage!!)
        }
    }

    @JpaOrder(4)
    @Test
    @Transactional
    fun `delete workflowstage that has role association`() {
        val workerManager = daoWorker.findByIdOrNull(4)!! // Makłowisz-NaśwManager
        val workflowStage =
            WorkflowStage(daoRole.findByIdOrNull(5)!!, "delete role that has association", mutableSetOf(workerManager))
        dao.saveAndFlush(workflowStage)
        assertNotNull(workflowStage.id)
        assertEquals("naświetlarnia", workflowStage.role.name)
        assertDoesNotThrow {
            dao.delete(workflowStage)
        }
    }

    @Test
    @JpaOrder(5)
    @Transactional
    fun `worker association test`() {
        val workflowStage = dao.findByIdOrNull(2)!!
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