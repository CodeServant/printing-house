package pl.macia.printinghouse.server.test.dao

import jakarta.transaction.Transactional
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.RoleDAO
import org.junit.jupiter.api.Assertions.*
import pl.macia.printinghouse.server.dao.EmployeeDAO

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class RoleDAOTest {
    @Autowired
    lateinit var rDao: RoleDAO

    @Autowired
    lateinit var eDao: EmployeeDAO

    @Order(1)
    @Test
    fun `find by id test`() {
        val role = rDao.findByIdOrNull(1)
        assertEquals("SALESMAN", role?.name)
    }

    @Test
    @Order(2)
    @Transactional
    fun `test join employee`() {
        val emp = eDao.findByIdOrNull(1)
        assertTrue(
            emp?.roles
                ?.map {
                    it.name
                }!!
                .contains("SALESMAN")
        )
        assertEquals("Kowalski-Salesman", rDao.findByIdOrNull(1)?.employees?.get(0)?.surname)
    }
}