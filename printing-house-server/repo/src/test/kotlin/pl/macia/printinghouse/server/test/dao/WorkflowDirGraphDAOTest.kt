package pl.macia.printinghouse.server.test.dao

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.WorkflowDirGraphDAO

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class WorkflowDirGraphDAOTest {
    @Autowired
    lateinit var dao: WorkflowDirGraphDAO

    @Test
    fun findById() {
        val found = dao.findByIdOrNull(1)!!
        assertEquals("typical flow", found.name)
        assertNull(found.comment)
        assertEquals(3, found.edges.size)
        val edge = found.edges.find {
            it.id == 1
        }!!
        assertEquals("Handlowiec", edge.v1.name)
    }
}