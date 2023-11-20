package pl.macia.printinghouse.server.test.controller

import org.hamcrest.Matchers
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.web.context.WebApplicationContext
import pl.macia.printinghouse.response.PaperTypeResp
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.PrintingHouseServerApplication

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaperTypeCTest {
    private lateinit var standardTest: StdCTest

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private val uri = "/${Paths.CONTEXT}/${Paths.PAPER_TYPES}"

    @BeforeAll
    fun setUp() {
        standardTest = StdCTest(uri, webApplicationContext)
    }

    @BeforeEach
    fun beforeEach() {
        standardTest.beforeEach()
    }

    @Test
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER])
    fun `get one by id`() {
        val papTpID = 1
        standardTest.checkFindOneById(
            papTpID,
            jsonPath("$.id").value(papTpID),
            jsonPath("$.name").value("Papier Błysk"),
            jsonPath("$.shortName").value("Pap Błysk")
        )
    }

    @Test
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER])
    fun `get all test`() {
        standardTest.checkGetAllFromPath(
            jsonPath("$").value(Matchers.hasSize<PaperTypeResp>(3)),
            jsonPath("$[*].name").value(Matchers.hasItems("Papier Błysk", "Papier Offsetowy", "papier Kredowy")),
            jsonPath("$[*].shortName").value(Matchers.hasItems("Pap Błysk", "Offsetowy", "kreda")),
            jsonPath("$[*].id").value(Matchers.hasItems(1, 2, 3))
        )
    }
}