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
        val v1 = workflowStageDAO.findByIdOrNull(v1ID)!!
        val v2 = workflowStageDAO.findByIdOrNull(1)!!
        fun assertEdge(exists: Boolean) {
            val edgeFound = found.edges.find {
                it.v1.name == v1.name && it.v2.name == v2.name
            }
            assertEquals(exists, edgeFound != null)
        }
        assertEdge(exists = false)
        found.addEdge(v1, v2)
        dao.save(found)
        found = dao.findByIdOrNull(1)!!
        assertEquals("typical flow", found.name)
        assertEdge(exists = true)
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
            workflowStageDAO.findByIdOrNull(2)!!,
            workflowStageDAO.findByIdOrNull(3)!!
        )
        dao.save(graph)
        graph = dao.findByIdOrNull(graph.id!!)!!
        assertNotNull(graph.id)
        assertEquals("Introligatornia", graph.edges.first().v1.name)
    }
}