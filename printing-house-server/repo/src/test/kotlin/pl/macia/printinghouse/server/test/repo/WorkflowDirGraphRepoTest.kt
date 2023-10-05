package pl.macia.printinghouse.server.test.repo

import jakarta.transaction.Transactional
import jakarta.validation.ValidationException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.bmodel.WorkflowDirGraph
import pl.macia.printinghouse.server.repository.WorkflowDirGraphIntRepo
import pl.macia.printinghouse.server.repository.WorkflowStageIntRepo
import java.time.LocalDateTime


@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
internal class WorkflowDirGraphRepoTest {
    @Autowired
    private lateinit var repo: WorkflowDirGraphIntRepo

    @Autowired
    private lateinit var workflowStageRepo: WorkflowStageIntRepo

    @Test
    @Transactional
    fun `find by id`() {
        val found = repo.findById(1)!!
        assertEquals("typical flow", found.name)
        assertNull(found.comment)
        val edge = found.edge.find { it.wEdgeId == 1 }!!
        assertEquals("Handlowiec", edge.v1.name)
        assertEquals("handlowiec", edge.v1.role.name)
        assertEquals("Introligatornia", edge.v2.name)
        assertEquals("introligatornia", edge.v2.role.name)
        assertEquals(found.name, edge.grapf.name)
    }

    @Test
    @Transactional
    fun `insert one test`() {
        var graph = WorkflowDirGraph(
            creationTime = LocalDateTime.now().minusHours(2),
            name = "insertOnenameTest",
            changedTime = LocalDateTime.now(),
            comment = "insert on test comment"
        )
        graph.addEdge(
            workflowStageRepo.findById(1)!!,
            workflowStageRepo.findById(2)!!
        )
        assertEquals(1, graph.edge.size)
        repo.save(graph)
        assertNotNull(graph.wGraphId) // saved correctly
        graph = repo.findById(graph.wGraphId!!)!!
        assertEquals("insertOnenameTest", graph.name)
        assertNotNull(graph.comment)
        assertEquals(1, graph.edge.size)
        graph.addEdge(
            workflowStageRepo.findById(2)!!,
            workflowStageRepo.findById(3)!!
        )
        repo.save(graph)
        graph = repo.findById(graph.wGraphId!!)!!
        assertEquals(2, graph.edge.size)
    }

    @Test
    fun `validating empty graph`() {
        assertThrows<ValidationException> {
            val graph = WorkflowDirGraph(
                creationTime = LocalDateTime.now().minusHours(2),
                name = "insertOnenameTest",
                changedTime = LocalDateTime.now(),
                comment = "insert on test comment"
            )
            repo.save(graph)
        }
    }
}