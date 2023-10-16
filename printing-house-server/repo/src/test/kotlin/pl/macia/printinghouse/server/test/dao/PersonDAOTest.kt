package pl.macia.printinghouse.server.test.dao

import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.PersonDAO
import pl.macia.printinghouse.server.dto.Person
import kotlin.jvm.optionals.getOrNull

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class PersonDAOTest {
    @Autowired
    lateinit var dao: PersonDAO

    @Order(1)
    @Test
    fun `find by id test`() {
        var person: Person? = null
        assertDoesNotThrow {
            person = dao.findById(4).getOrNull()
        }
        assertEquals("Robert", person?.name)
        assertEquals("Makłowisz-NaśwManager", person?.surname)
        assertEquals("42389285u78", person?.pseudoPESEL)
    }

    @Test
    fun `constraint test`() {
        val nameLength = 200
        val surnameLength = 300
        val peselLength = 11

        fun savNFlush(nL: Int, sL: Int, pL: Int, padChar: Char) {
            dao.saveAndFlush(
                Person(
                    "".padEnd(nL, padChar),
                    "".padEnd(sL, padChar),
                    "".padEnd(pL, padChar)
                )
            )
        }

        assertDoesNotThrow { savNFlush(nameLength, surnameLength, peselLength, 'a') }
        assertDoesNotThrow { savNFlush(nameLength - 1, surnameLength - 1, peselLength, 'b') }
        assertThrows<ConstraintViolationException> { savNFlush(nameLength, surnameLength, peselLength, ' ') }
        assertThrows<ConstraintViolationException> { savNFlush(nameLength + 1, surnameLength, peselLength, 'c') }
        assertThrows<ConstraintViolationException> { savNFlush(nameLength, surnameLength + 1, peselLength, 'd') }
        assertThrows<ConstraintViolationException> { savNFlush(nameLength, surnameLength, peselLength + 1, 'e') }
        assertThrows<ConstraintViolationException> { savNFlush(nameLength, surnameLength, peselLength - 1, 'f') }
    }
}