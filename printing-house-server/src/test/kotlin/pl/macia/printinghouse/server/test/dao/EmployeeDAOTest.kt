package pl.macia.printinghouse.server.test.dao

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.EmployeeDAO
import pl.macia.printinghouse.server.dto.Email
import pl.macia.printinghouse.server.dto.Employee

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class EmployeeDAOTest {
    @Autowired
    lateinit var dao: EmployeeDAO

    @Order(1)
    @Test
    fun `find by id test`() {
        val emp = dao.findByIdOrNull(1)

        assertEquals("Jan", emp?.name)
    }

    @Order(2)
    @Test
    fun `create employee test`() {
        var emp = Employee(
            Email("newEmp@wp.pl"),
            "{bcrypt}\$2a\$12\$onVIlBR/EoHYej8KAvgGYekLQS4/IKVnseD89eYT5YMNjoK3r25W.",
            true,
            true,
            "NewGuy",
            "McVerynew",
            "99999999999"
        )
        dao.saveAndFlush(emp)

        assertTrue(dao.existsById(emp.id!!))

        emp = Employee(
            Email("newEmp@wp.pl"),
            "{bcrypt}\$2a\$12\$onVIlBR/EoHYej8KAvgGYekLQS4/IKVnseD89eYT5YMNjoK3r25W.",
            true,
            true,
            "NewGuy",
            "McVerynew",
            "99999999999"
        )
        assertThrows<DataIntegrityViolationException> {
            dao.saveAndFlush(emp)
        }
    }
}