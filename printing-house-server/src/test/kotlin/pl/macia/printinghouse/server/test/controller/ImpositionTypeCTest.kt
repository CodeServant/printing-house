package pl.macia.printinghouse.server.test.controller

import jakarta.transaction.Transactional
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hamcrest.Matchers
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.web.context.WebApplicationContext
import pl.macia.printinghouse.request.ImpositionTypeChangeReq
import pl.macia.printinghouse.response.PrinterResp
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import pl.macia.printinghouse.request.ImpositionTypeReq

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ImpositionTypeCTest {
    private lateinit var standardTest: StdCTest

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private val uri = "/${Paths.CONTEXT}/${Paths.IMPOSITION_TYPES}"

    @BeforeAll
    fun setUp() {
        standardTest = StdCTest(uri, webApplicationContext)
    }

    @BeforeEach
    fun beforeEach() {
        standardTest.beforeEach()
    }

    @Test
    @WithMockUser("juliusz@example.com", authorities = [PrimaryRoles.MANAGER])
    fun `get one by id`() {
        val dkID = 1
        standardTest.checkFindOneById(
            dkID,
            jsonPath("$.id").value(dkID),
            jsonPath("$.name").value("f/f")
        )
    }

    @Test
    @WithMockUser("juliusz@example.com", authorities = [PrimaryRoles.MANAGER])
    fun `get all test`() {
        standardTest.checkGetAllFromPath(
            jsonPath("$").value(Matchers.hasSize<PrinterResp>(3)),
            jsonPath("$[*].name").value(
                Matchers.hasItems("f/f", "f/g", "f/o")
            ),
            jsonPath("$[*].id").value(Matchers.hasItems(1, 2, 3))
        )
    }

    @Test
    @Transactional
    @WithMockUser("juliusz@example.com", authorities = [PrimaryRoles.MANAGER])
    fun `insert one printer`() {
        val example = ImpositionTypeReq(
            "d/r"
        )
        standardTest.checkInsertOneObj(
            Json.encodeToString(example),
            "id",
            jsonPath("$.name").value("d/r"),
            jsonPath("$.id").isNumber,
        )
    }

    @Test
    @Transactional
    @WithMockUser("juliusz@example.com", authorities = [PrimaryRoles.MANAGER])
    fun `change ImpositionType test`() {
        val impTypId = 1
        val changeReq = ImpositionTypeChangeReq(
            name = "p/tr"
        )
        standardTest.checkChangeObjTest(
            impTypId,
            Json.encodeToString(changeReq),
            jsonPath("$.name").value("p/tr")
        )
    }
}