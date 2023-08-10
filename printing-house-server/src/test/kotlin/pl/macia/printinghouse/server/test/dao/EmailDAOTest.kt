package pl.macia.printinghouse.server.test.dao

import jakarta.validation.ConstraintViolationException
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.EmailDAO
import pl.macia.printinghouse.server.dto.Email

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class EmailDAOTest {
    @Autowired
    lateinit var dao: EmailDAO

    @Order(1)
    @Test
    fun `get by id test`() {
        assertEquals("anna@wp.pl", dao.findByIdOrNull(1)?.email)
    }

    @Order(2)
    @Test
    fun `create test`() {
        val maxEmailUsernameLen = 64 // the biggest number of email name section can have
        assertDoesNotThrow {
            dao.saveAndFlush(Email("inserted@gmail.com"))
        }
        assertThatExceptionOfType(ConstraintViolationException::class.java).isThrownBy {
            dao.saveAndFlush(Email("should not be inserted@@gmail.com"))
        }
        assertThatExceptionOfType(ConstraintViolationException::class.java).isThrownBy {
            val end = "@gmail.com"
            val email = "a".repeat(maxEmailUsernameLen + 1) + end
            dao.saveAndFlush(Email(email))
        }
        assertThatNoException().isThrownBy {
            val end = "@gmail.com"
            val email = "a".repeat(maxEmailUsernameLen) + end
            dao.saveAndFlush(Email(email))
        }
        // now email is doubled
        assertThatExceptionOfType(DataIntegrityViolationException::class.java).isThrownBy {
            dao.saveAndFlush(Email("inserted@gmail.com"))
        }
    }
}