package pl.macia.printinghouse.server.test.dao

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.RoleDAO
import pl.macia.printinghouse.server.dao.WorkflowStageDAO
import pl.macia.printinghouse.server.dto.*

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class WorkflowStageDAOTest {
    @Autowired
    lateinit var dao: WorkflowStageDAO

    @Autowired
    lateinit var daoRole: RoleDAO

    @Order(1)
    @Test
    fun `get by id`() {
        val workflowStage = dao.findByIdOrNull(1)
        assertEquals("Introligatornia", workflowStage?.name)
    }

    @Order(2)
    @Test
    @Transactional
    fun `role association test`() {
        val newWorkflowStage =
            WorkflowStage(Role("newRoleToWorkflowStage role association test"), "introligatorNewWorkflowStage test")
        dao.saveAndFlush(newWorkflowStage)
        assertNotNull(newWorkflowStage.id, "$tableWorkflowStage not created")
        assertNotNull(newWorkflowStage.role.id, "$tableRole not created")
        newWorkflowStage.role = Role("newRoleToWorkflowStage")
        dao.saveAndFlush(newWorkflowStage)
        assertNotNull(newWorkflowStage.role.id)
        val deletedId = newWorkflowStage.role.id!!
        dao.delete(newWorkflowStage)
        assertNull(dao.findByIdOrNull(deletedId))
    }

    @Order(3)
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

    @Order(4)
    @Test
    @Transactional
    fun `delete workflowstage that has role association`() {
        val workflowStage = WorkflowStage(daoRole.findByIdOrNull(5)!!, "delete role that has association")
        dao.saveAndFlush(workflowStage)
        assertNotNull(workflowStage.id)
        assertEquals("naświetlarnia",workflowStage.role.name)
        assertDoesNotThrow {
            dao.delete(workflowStage)
        }
    }
}