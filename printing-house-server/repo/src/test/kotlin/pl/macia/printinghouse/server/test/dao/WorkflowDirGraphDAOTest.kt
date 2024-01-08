package pl.macia.printinghouse.server.test.dao

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.WorkflowDirGraphDAO
import pl.macia.printinghouse.server.dao.WorkflowStageDAO
import pl.macia.printinghouse.server.dto.WorkflowDirGraph
import java.time.LocalDateTime

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class WorkflowDirGraphDAOTest {
    @Autowired
    private lateinit var dao: WorkflowDirGraphDAO

    @Autowired
    private lateinit var workflowStageDAO: WorkflowStageDAO

    @Test
    @Transactional
    fun findById() {
        val found = dao.findByIdOrNull(1)!!
        assertEquals("typical flow", found.name)
        assertNull(found.comment)
        assertEquals(4, found.edges.size)
        val edge = found.edges.find {
            it.id == 1
        }!!
        assertEquals("Handlowiec", edge.v1.name)
    }

    @Test
    @Transactional
    fun `new edge test`() {
        var found = dao.findByIdOrNull(1)!!
        val v1ID = 4
        val edge = found.addEdge(workflowStageDAO.findByIdOrNull(v1ID)!!, workflowStageDAO.findByIdOrNull(1)!!)
        dao.save(found)
        found = dao.findByIdOrNull(1)!!
        assertEquals("typical flow", found.name)
        assertNotNull(edge.id)
    }

    @Test
    @Transactional
    fun `insert one test`() {
        var graph = WorkflowDirGraph(
            id = null,
            creationTime = LocalDateTime.now().minusHours(2),
            name = "insertOnenameTest",
            changedTime = LocalDateTime.now(),
            comment = "insert on test comment"
        )
        graph.addEdge(
            workflowStageDAO.findByIdOrNull(1)!!,
            workflowStageDAO.findByIdOrNull(2)!!
        )
        dao.save(graph)
        graph = dao.findByIdOrNull(graph.id)!!
        assertNotNull(graph.id)
        assertEquals("Introligatornia", graph.edges.first().v1.name)
    }
}