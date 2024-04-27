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
import pl.macia.printinghouse.request.PrinterChangeReq
import pl.macia.printinghouse.request.PrinterReq
import pl.macia.printinghouse.response.PrinterResp
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.PrintingHouseServerApplication

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PrinterCTest {
    private lateinit var standardTest: StdCTest

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private val uri = "/${Paths.CONTEXT}/${Paths.PRINTERS}"

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
            jsonPath("$.name").value("duża komori"),
            jsonPath("$.digest").value("DK")
        )
    }

    @Test
    @WithMockUser("juliusz@example.com", authorities = [PrimaryRoles.MANAGER])
    fun `get all test`() {
        standardTest.checkGetAllFromPath(
            jsonPath("$").value(Matchers.hasSize<PrinterResp>(2)),
            jsonPath("$[*].name").value(Matchers.hasItems("duża komori", "mała komori")),
            jsonPath("$[*].digest").value(Matchers.hasItems("DK", "MK")),
            jsonPath("$[*].id").value(Matchers.hasItems(1, 2))
        )
    }

    @Test
    @Transactional
    @WithMockUser("juliusz@example.com", authorities = [PrimaryRoles.MANAGER])
    fun `insert one printer`() {
        val dummyPrinter = PrinterReq(
            "Normal Printer",
            "NP"
        )
        standardTest.checkInsertOneObj(
            Json.encodeToString(dummyPrinter),
            "id",
            jsonPath("$.name").value("Normal Printer"),
            jsonPath("$.digest").value("NP"),
            jsonPath("$.id").isNumber,
        )
    }

    @Test
    @Transactional
    @WithMockUser("juliusz@example.com", authorities = [PrimaryRoles.MANAGER])
    fun `change test printer`() {
        val printerId = 1
        val changeReq = PrinterChangeReq(
            name = "some name"
        )
        standardTest.checkChangeObjTest(
            printerId,
            Json.encodeToString(changeReq),
            jsonPath("$.name").value("some name"),
            jsonPath("$.digest").value("DK")
        )
    }
}