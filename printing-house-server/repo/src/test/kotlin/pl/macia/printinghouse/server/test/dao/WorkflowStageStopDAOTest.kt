package pl.macia.printinghouse.server.test.dao

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.*
import pl.macia.printinghouse.server.dao.OrderDAO
import pl.macia.printinghouse.server.dao.WorkerDAO
import pl.macia.printinghouse.server.dao.WorkflowStageDAO
import pl.macia.printinghouse.server.dao.WorkflowStageStopDAO
import java.time.LocalDateTime

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
internal class WorkflowStageStopDAOTest {

    @Autowired
    lateinit var dao: WorkflowStageStopDAO

    @Autowired
    lateinit var daoWorker: WorkerDAO

    @Autowired
    lateinit var daoWorkflowStage: WorkflowStageDAO

    @Autowired
    private lateinit var daoOrder: OrderDAO

    @Autowired
    private lateinit var graphDAO: WorkflowDirGraphDAO

    @Test
    fun `find by id test`() {
        val found = dao.findByIdOrNull(1)!!
        assertEquals("coś zrobiłam i nawet dobrze", found.comment)
        val date = LocalDateTime.of(2022, 10, 21, 15, 0)
        assertEquals(date, found.createTime)
        assertEquals("Anna", found.worker?.name)
        assertEquals("Naświetlarnia", found.workflowStage.name)
    }

    @Test
    @Transactional
    fun `delete one testing`() {
        val initQ = dao.count()
        dao.deleteById(1)
        assertEquals(1, initQ - dao.count())
    }

    @Test
    fun `test not changing the dependants`() {
        val found = dao.findByIdOrNull(1)!!
        found.worker?.name = "Anna2"
        found.workflowStage.name = "Naświetlarnia2"
        dao.saveAndFlush(found)
        val actualWorker = daoWorker.findByIdOrNull(found.worker?.id)!!
        val actualWorkflowStage = daoWorkflowStage.findByIdOrNull(found.workflowStage.id)!!
        // names should remain the same
        assertEquals("Anna", actualWorker.name)
        assertEquals("Naświetlarnia", actualWorkflowStage.name)
    }

    @Test
    @Transactional
    fun `insert single`() {
        val order = daoOrder.findByIdOrNull(1)!!
        val workflowStage = daoWorkflowStage.findByIdOrNull(2)!!

        val graph = graphDAO.findByIdOrNull(1)!!
        val edge = graph.edges.find { it.id == 1 }!!
        val workflowStageStop = order.addWorkflowStageStop(
            null,
            LocalDateTime.now(),
            null,
            null,
            workflowStage,
            edge
        )
        assertNull(workflowStageStop.id)
        dao.save(workflowStageStop)
        assertNotNull(workflowStageStop.id)
        assertEquals("Handlowiec", edge.v1.name)
    }
}