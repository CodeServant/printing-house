package pl.macia.printinghouse.server.test.dao

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.PersonDAO
import pl.macia.printinghouse.server.dao.WorkerDAO
import pl.macia.printinghouse.server.dto.Email
import pl.macia.printinghouse.server.dto.Worker
import kotlin.jvm.optionals.getOrNull

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class WorkerDAOTest {
    @Autowired
    lateinit var dao: WorkerDAO

    @Autowired
    lateinit var personDAO: PersonDAO

    @Test
    @Order(1)
    fun `find by id test`() {
        val workers = dao.findAllById(listOf(1, 2, 3, 4))
        val expectedNames = listOf("Marian", "Juliusz", "Robert")
        assertIterableEquals(expectedNames, workers.map { it.name })
        val firstWkr = workers.first()!!
        assertNotNull(firstWkr.roles.find { it.name == "krajalnia" })
    }

    @Test
    @Order(1)
    fun `inheritance test`() {
        val worker = dao.findByIdOrNull(7)
        val person = personDAO.findByIdOrNull(7)
        assertEquals(worker?.id, person?.id)
        assertEquals(worker?.name, person?.name)
    }

    @Test
    @Order(2)
    fun `insert new one`() {
        val newWork = Worker(
            Email("insertNewOneWorkerTest@wp.pl"),
            "{bcrypt}\$2a\$12\$onVIlBR/EoHYej8KAvgGYekLQS4/IKVnseD89eYT5YMNjoK3r25W.",
            activeAccount = true,
            employed = true,
            name = "SomeNewGuy",
            surname = "McVerynew",
            pseudoPESEL = "25322246s13"
        )
        assertDoesNotThrow {
            dao.save(newWork)
        }
    }

    @Test
    @Order(3)
    @Transactional
    fun `delete one test`() {
        assertTrue(dao.existsById(7))
        dao.deleteById(7)
        assertFalse(dao.existsById(7))
    }

    @Test
    @Order(4)
    @Transactional
    fun `workflow manager association test`() {
        val workerManager = dao.findByIdOrNull(4)!!
        assertEquals(
            2, workerManager
                .isManagerOf
                .stream()
                .findFirst()
                .getOrNull()
                ?.id
        )
    }

    @Test
    fun `search query test`() {
        val workers = dao.searchForName("uliusz")
        assertEquals(1, workers.size)
        assertEquals(workers[0].name, "Juliusz")
        assertEquals(workers[0].surname, "Szymański-Introligatornia")
    }
}
