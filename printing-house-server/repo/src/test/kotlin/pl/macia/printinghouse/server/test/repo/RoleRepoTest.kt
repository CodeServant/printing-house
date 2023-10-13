package pl.macia.printinghouse.server.test.repo

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.bmodel.Role
import pl.macia.printinghouse.server.repository.RoleIntRepo

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
internal class RoleRepoTest {
    @Autowired
    lateinit var repo: RoleIntRepo

    @Test
    fun `find by id test`() {
        var role = repo.findById(1)
        assertEquals("SALESMAN", role?.name)
        role = repo.findById(2)
        assertEquals("introligatornia", role?.name)
        role = repo.findById(100)
        assertNull(role)
    }

    @Test
    @Transactional
    fun save() {
        val new = Role("newSaveRoleTestRepo")
        SingleIdTests<Role, Int>(repo).createNew(new, new::roleId, repo::findById)
    }
}