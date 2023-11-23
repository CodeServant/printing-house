package pl.macia.printinghouse.server.test.controller

import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.web.context.WebApplicationContext
import pl.macia.printinghouse.response.EnoblingResp
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.PrintingHouseServerApplication

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EnoblingCTest {
    private lateinit var standardTest: StdCTest

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private val uri = "/${Paths.CONTEXT}/${Paths.ENOBLINGS}"

    @BeforeAll
    fun setUp() {
        standardTest = StdCTest(uri, webApplicationContext)
    }

    @BeforeEach
    fun beforeEach() {
        standardTest.beforeEach()
    }

    @Test
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.EMPLOYEE])
    fun `get one by id`() {
        fun testId(id: Int, name: String, desc: String?, type: String) {
            standardTest.checkFindOneById(
                id,
                MockMvcResultMatchers.jsonPath("$.id").value(id),
                MockMvcResultMatchers.jsonPath("$.name").value(name),
                MockMvcResultMatchers.jsonPath("$.description").value(desc),
                MockMvcResultMatchers.jsonPath("$.type").value(type)
            )
        }
        testId(1, "farba kolorowa", "to jest farba kolorowa", "UVVarnish")
        testId(4, "szalony wykrojnik", null, "Punch")
    }

    @Test
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER])
    fun `get all test`() {
        standardTest.checkGetAllFromPath(
            MockMvcResultMatchers.jsonPath("$").value(Matchers.hasSize<EnoblingResp>(5)),
            MockMvcResultMatchers.jsonPath("$[*].name").value(
                Matchers.hasItems(
                    "farba kolorowa",
                    "karton aksamitny",
                    "papier błysk",
                    "szalony wykrojnik",
                    "wykrojnik zwyczajny"
                )
            ),
            MockMvcResultMatchers.jsonPath("$[*].description").value(
                Matchers.hasItems(
                    "to jest farba kolorowa",
                    "taki sobie oto wykrojnik zwyczajny"
                )
            ),
            MockMvcResultMatchers.jsonPath("$[?(@.description!==null)]").value(
                Matchers.hasSize<String>(2)
            ),
            MockMvcResultMatchers.jsonPath("$[?(@.type == 'UVVarnish')]").value(Matchers.hasSize<String>(1)),
            MockMvcResultMatchers.jsonPath("$[?(@.type == 'Punch')]").value(Matchers.hasSize<String>(2)),
            MockMvcResultMatchers.jsonPath("$[?(@.type == 'Enobling')]").value(Matchers.hasSize<String>(2))
        )
    }
}