package pl.macia.printinghouse.server.test.dao

import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.dao.BindingFormDAO
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Example
import pl.macia.printinghouse.server.dto.BindingForm

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class BindingFormTest {
    @Autowired
    lateinit var dao: BindingFormDAO

    @Test
    fun `find by id test`() {
        assertEquals("Folia", dao.findByIdOrNull(2)?.name)
        assertEquals("Papier", dao.findByIdOrNull(1)?.name)
    }

    @Test
    fun `constraints validation tests`() {
        val nameLimit = 200
        val ppTypeName = "PanamaPaper"
        assertDoesNotThrow { dao.saveAndFlush(BindingForm(ppTypeName)) }
        assertThrows<DataIntegrityViolationException> {
            dao.saveAndFlush(BindingForm(ppTypeName))
        }
        assertEquals(
            1,
            dao.count(Example.of(BindingForm(ppTypeName))),
            "duplicated ${BindingForm.bindingFormName} field"
        )
        assertThrows<ConstraintViolationException> {
            val toLong = BindingForm(generateLongName(nameLimit + 1))
            dao.saveAndFlush(toLong)
        }
        assertDoesNotThrow {
            val acurate = BindingForm(generateLongName(nameLimit))
            dao.saveAndFlush(acurate)
        }
    }
}