package pl.macia.printinghouse.server.test.controller

import jakarta.transaction.Transactional
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.web.context.WebApplicationContext
import pl.macia.printinghouse.request.BindingFormReq
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.response.WorkerResp
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.PrintingHouseServerApplication

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BindingFormCTest {
    private lateinit var standardTest: StdCTest

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private val uri = "/${Paths.CONTEXT}/${Paths.BINDING_FORMS}"

    @BeforeAll
    fun setUp() {
        standardTest = StdCTest(uri, webApplicationContext)
    }

    @BeforeEach
    fun beforeEach() {
        standardTest.beforeEach()
    }

    @Test
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.SALESMAN])
    fun `find all binding forms`() {
        standardTest.checkGetAllFromPath(
            jsonPath("$[*].name").value(Matchers.hasItems("Folia", "Karton", "Papier")),
            jsonPath("$.*").value(Matchers.hasSize<List<WorkerResp>>(4))
        )
    }

    @Test
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER])
    fun `get one by id`() {
        standardTest.checkFindOneById(
            2,
            jsonPath("$.id").value(2),
            jsonPath("$.name").value("Folia")
        )
    }

    fun dummyBindingForm(name: String): BindingFormReq {
        return BindingFormReq(name)
    }

    fun insertBindingForm(name: String): RecID {
        val bindFormReq = dummyBindingForm(name)

        return standardTest.checkInsertOneObj(
            Json.encodeToString(bindFormReq),
            "id",
            jsonPath("$.name").value(name)
        )
    }

    @Test
    @Transactional
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER, PrimaryRoles.SALESMAN])
    fun `insert one test`() {
        insertBindingForm("insertOneTestController")
    }
}