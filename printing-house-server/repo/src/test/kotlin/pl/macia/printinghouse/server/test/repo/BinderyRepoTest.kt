package pl.macia.printinghouse.server.test.repo

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.bmodel.Bindery
import pl.macia.printinghouse.server.repository.BinderyRepo

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
class BinderyRepoTest {
    @Autowired
    lateinit var dao: BinderyRepo

    @Test
    fun `find by id test`() {
        val found = dao.findById(1)!!
        assertEquals("A1", found.name)
    }

    @Test
    @Transactional
    fun `create new`() {
        val new = Bindery("B2")
        SingleIdTests<Bindery,Int>(dao).createNew(new, new::binderyId, dao::findById)
    }
}