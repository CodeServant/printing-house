package pl.macia.printinghouse.server.test.bmodel

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import pl.macia.printinghouse.server.bmodel.Email
import pl.macia.printinghouse.server.dto.Email as PEmail
import pl.macia.printinghouse.server.bmodel.EmailImpl
import pl.macia.printinghouse.server.bmodel.BMutableList
import pl.macia.printinghouse.server.bmodel.BMutableSet


internal class CollectionTests {
    val toPer: (Email) -> PEmail = {
        it as EmailImpl
        it.persistent
    }
    val toBiz: (PEmail) -> Email = ::EmailImpl

    @Test
    fun `test transform functions`() {
        val email = Email("someEmail@gmail.com")
        assertEquals("someEmail@gmail.com", toPer(email).email)
        assertEquals("someEmail@gmail.com", toBiz(toPer(email)).email)
    }

    @Test
    fun `BMutableSet tests`() {
        val email = Email("someEmail@gmail.com")
        val set: MutableSet<Email> = BMutableSet(toBiz, toPer)
        assertTrue(set.add(email))
        assertEquals(1, set.size)
        assertTrue(set.contains(email))
    }

    @Test
    fun `BMutableList tests`() {
        val email = Email("someEmail@gmail.com")
        val list: MutableList<Email> = BMutableList(toBiz, toPer)
        assertTrue(list.add(email))
        assertEquals(1, list.size)
        assertTrue(list.contains(email))
        assertNotEquals(Email("someEmail@gmail.com"), list[0])
    }
}