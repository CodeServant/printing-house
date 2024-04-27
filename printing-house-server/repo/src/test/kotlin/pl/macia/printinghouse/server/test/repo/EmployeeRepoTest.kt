package pl.macia.printinghouse.server.test.repo

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.repository.EmployeeIntRepo

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
internal class EmployeeRepoTest {
    @Autowired
    lateinit var repo: EmployeeIntRepo

    @Test
    fun `find by email`() {
        val employee = repo.findByEmail("jan@example.com")
        assertEquals("Jan", employee?.name)
    }
}