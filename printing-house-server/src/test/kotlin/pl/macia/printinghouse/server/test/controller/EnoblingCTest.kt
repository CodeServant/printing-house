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
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.web.context.WebApplicationContext
import pl.macia.printinghouse.request.*
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
    @WithMockUser("juliusz@example.com", authorities = [PrimaryRoles.EMPLOYEE])
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
    @WithMockUser("juliusz@example.com", authorities = [PrimaryRoles.MANAGER])
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

    @Test
    @Transactional
    @WithMockUser("juliusz@example.com", authorities = [PrimaryRoles.MANAGER, PrimaryRoles.EMPLOYEE])
    fun `insert one enobling`() {
        fun checkEnobling(name: String, desc: String, type: String, constr: (String, String) -> IEnoblingReq) {
            val dummyEnobling = constr(name, desc)
            standardTest.checkInsertOneObj(
                Json.encodeToString(dummyEnobling),
                "id",
                MockMvcResultMatchers.jsonPath("$.name").value(name),
                MockMvcResultMatchers.jsonPath("$.description").value(desc),
                MockMvcResultMatchers.jsonPath("$.type").value(type),
                MockMvcResultMatchers.jsonPath("$.id").isNumber,
            )
        }
        checkEnobling("RegularEnobling", "some regular enobling", "Enobling", ::EnoblingReq)
        checkEnobling("RegularEnobling2", "some regular enobling2", "UVVarnish", ::UVVarnishReq)
        checkEnobling("RegularEnobling3", "some regular enobling3", "Punch", ::PunchReq)
    }

    @Test
    @Transactional
    @WithMockUser("juliusz@example.com", authorities = [PrimaryRoles.MANAGER, PrimaryRoles.EMPLOYEE])
    fun `change one test`() {
        var req: IEnoblingChangeReq = PunchChangeReq(
            name = "somename"
        )
        val type = MockMvcResultMatchers.jsonPath("$.type")
        val name = MockMvcResultMatchers.jsonPath("$.name")
        val description = MockMvcResultMatchers.jsonPath("$.description")
        fun standardTest(id: Int, typeName: String, nameName: String, resultMatch: ResultMatcher) {
            standardTest.checkChangeObjTest(
                id,
                Json.encodeToString(req),
                type.value(typeName),
                name.value(nameName),
                resultMatch
            )
        }
        standardTest(4, "Punch", "somename", description.doesNotExist())
        req = UVVarnishChangeReq(
            description = "some description",
            nullingRest = true
        )
        standardTest(1, "UVVarnish", "farba kolorowa", description.value("some description"))
        req = EnoblingChangeReq(
            description = "some description",
            nullingRest = true
        )
        standardTest(2, "Enobling", "karton aksamitny", description.value("some description"))
    }
}