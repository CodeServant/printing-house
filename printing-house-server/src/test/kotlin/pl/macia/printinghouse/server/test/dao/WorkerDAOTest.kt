package pl.macia.printinghouse.server.test.dao

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

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class WorkerDAOTest {
    @Autowired
    lateinit var workerDAO: WorkerDAO

    @Autowired
    lateinit var personDAO: PersonDAO

    @Test
    @Order(1)
    fun `find by id test`() {
        val workers = workerDAO.findAllById(listOf(1, 2, 3, 4))
        val expectedNames = listOf("Marian", "Jiliusz", "Robert")
        assertIterableEquals(expectedNames, workers.map { it.name })
    }

    @Test
    @Order(1)
    fun `inheritance test`() {
        val worker = workerDAO.findByIdOrNull(7)
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
            workerDAO.save(newWork)
        }
    }

    @Test
    @Order(3)
    fun `delete one test`() {
        workerDAO.deleteById(7)
        assertFalse(workerDAO.existsById(7))
    }
}
