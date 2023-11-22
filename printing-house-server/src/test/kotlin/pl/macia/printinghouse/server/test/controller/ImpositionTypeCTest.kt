package pl.macia.printinghouse.server.test.controller

import org.hamcrest.Matchers
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.web.context.WebApplicationContext
import pl.macia.printinghouse.response.PrinterResp
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.PrintingHouseServerApplication

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
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER])
    fun `get one by id`() {
        val dkID = 1
        standardTest.checkFindOneById(
            dkID,
            jsonPath("$.id").value(dkID),
            jsonPath("$.name").value("f/f")
        )
    }

    @Test
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER])
    fun `get all test`() {
        standardTest.checkGetAllFromPath(
            jsonPath("$").value(Matchers.hasSize<PrinterResp>(3)),
            jsonPath("$[*].name").value(
                Matchers.hasItems("f/f", "f/g", "f/o")
            ),
            jsonPath("$[*].id").value(Matchers.hasItems(1, 2, 3))
        )
    }
}