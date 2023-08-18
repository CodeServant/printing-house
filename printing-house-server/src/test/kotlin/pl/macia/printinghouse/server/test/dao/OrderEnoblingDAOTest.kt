package pl.macia.printinghouse.server.test.dao

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.BinderyDAO
import pl.macia.printinghouse.server.dao.EnoblingDAO
import pl.macia.printinghouse.server.dao.OrderEnoblingDAO

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class OrderEnoblingDAOTest {
    //todo insertion test but after implementing order
    @Autowired
    lateinit var dao: OrderEnoblingDAO

    @Autowired
    lateinit var enoblingDAO: EnoblingDAO

    @Autowired
    lateinit var binderyDAO: BinderyDAO

    @Test
    fun `find by id test`() {
        val found = dao.findByIdOrNull(1)!!
        assertEquals("szalony wykrojnik", found.enobling.name)
        assertEquals("A2", found.bindery.name)
    }

    @Test
    @Transactional
    fun `delete one test`() {
        val found = dao.findByIdOrNull(1)!!
        val enobling = found.enobling
        val bindery = found.bindery
        val quan = dao.count()
        dao.delete(found)
        assertNotNull(enoblingDAO.findByIdOrNull(enobling.id))
        assertNotNull(binderyDAO.findByIdOrNull(bindery.id))
        assertEquals(1, quan - dao.count())
    }

    @Test
    fun `not changing dependants test`() {
        var found = dao.findByIdOrNull(1)!!
        found.enobling.name = "changed but should not to"
        found = dao.saveAndFlush(found)
        // the name in the database should remain the same as it was
        assertEquals("szalony wykrojnik", enoblingDAO.findByIdOrNull(found.enobling.id)?.name)
        found.bindery.name = "CHANGED1"
        found = dao.saveAndFlush(found)
        assertEquals("A2", binderyDAO.findByIdOrNull(found.bindery.id)?.name)
    }
}