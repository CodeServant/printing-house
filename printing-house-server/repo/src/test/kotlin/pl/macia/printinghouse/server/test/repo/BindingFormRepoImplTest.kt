package pl.macia.printinghouse.server.test.repo

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.server.bmodel.BindingForm
import pl.macia.printinghouse.server.repository.BindingFormIntRepo

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@TestPropertySource("classpath:inMemDB.properties")
internal class BindingFormRepoImplTest {
    @Autowired
    lateinit var repo: BindingFormIntRepo

    @Test
    fun `find by id test`() {
        val found = repo.findById(1)!!
        assertEquals("Papier", found.name)
    }

    @Test
    @Transactional
    fun `create new test`() {
        val new = BindingForm("thinPaperBindingFormRepoImplTestFindById")
        SingleIdTests<BindingForm, Int>(repo).createNew(new, new::bindingFormId, repo::findById)
    }
}