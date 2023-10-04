package pl.macia.printinghouse.server.test.repo

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.repository.WorkflowDirGraphIntRepo

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
internal class WorkflowDirGraphRepoTest {
    @Autowired
    lateinit var repo: WorkflowDirGraphIntRepo

    @Test
    @Transactional
    fun `find by id`() {
        val found = repo.findById(1)!!
        assertEquals("typical flow", found.name)
        assertNull(found.comment)
        val edge = found.edge.find { it.wEdgeId==1 }!!
        assertEquals("Handlowiec", edge.v1.name)
        assertEquals("handlowiec", edge.v1.role.name)
        assertEquals("Introligatornia", edge.v2.name)
        assertEquals("introligatornia", edge.v2.role.name)
        assertEquals(found.name, edge.grapf.name)
    }

    @Test
    fun `inesrt one test`() {
        TODO("Not yet implemented")
    }
}