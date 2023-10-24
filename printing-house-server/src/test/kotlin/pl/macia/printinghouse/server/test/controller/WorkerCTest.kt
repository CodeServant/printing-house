package pl.macia.printinghouse.server.test.controller

import com.jayway.jsonpath.JsonPath
import jakarta.transaction.Transactional
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import pl.macia.printinghouse.request.WorkerChangeReq
import pl.macia.printinghouse.request.WorkerReq
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.response.RoleResp
import pl.macia.printinghouse.response.WorkerResp
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.PrintingHouseServerApplication

// todo there lost of duplicated and almost the same code in each test it is good idea to create some common tools
/**
 * Constants defined to reduce redundancy in tested code. Constants defined in controllers are not defined here because api may change.
 */
object Paths {
    const val BINDING_FORMS = "binding-forms"
    const val WORKERS = "workers"
    const val CONTEXT = "api"
    const val SALESMANS = "salesmans"
    const val BINDERIES = "binderies"
    const val WORKFLOW_STAGES = "workflow-stages"
    const val CLIENTS = "clients"
}

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@WebAppConfiguration
internal class WorkerCTest {
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private val uri = "/${Paths.CONTEXT}/${Paths.WORKERS}"
    fun dummyWorkerTravolta(pesel: String, email: String): WorkerReq {
        return WorkerReq(
            isManagerOf = listOf(),
            employed = true,
            activeAccount = true,
            password = "123",
            email = email,
            psudoPESEL = pesel,
            surname = "Travolta",
            name = "John"
        )
    }

    @BeforeEach
    fun beforeEach() {
        mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
            .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
            .build()
    }

    @Test
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER])
    fun `get all workers test`() {
        mvc.perform(MockMvcRequestBuilders.get(uri))
            .andExpect { status().isOk }
            .andExpectAll(
                jsonPath("$[1].name").value("Jiliusz"),
                jsonPath("$[1].id").value(3),
                jsonPath("$.*").value(Matchers.hasSize<List<WorkerResp>>(4))
            )
    }

    @Test
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER])
    fun `find one test`() {
        fun perform(id: Int) = mvc.perform(MockMvcRequestBuilders.get("$uri/{id}", id))
        perform(3)
            .andExpect(status().isOk)
            .andExpectAll(
                jsonPath("$.name").value("Jiliusz"),
                jsonPath("$.id").value(3),
                jsonPath("$.roles[*].name").value(Matchers.hasItem(PrimaryRoles.WORKER)),
                jsonPath("$.roles[*].name").value(Matchers.hasItem(PrimaryRoles.EMPLOYEE))
            )
        perform(1)
            .andExpect(status().isNotFound)
    }

    /**
     * Inserts and checks if property interacted worker travolta.
     */
    fun insertTravolta(): RecID {
        val workerReq = dummyWorkerTravolta("72827641678", "deleteWorkerTest@example.com")
        val res = mvc.perform(
            MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON)
                .content(Json.encodeToString(workerReq))
        ).andExpect(status().isOk)
            .andReturn()

        //chack if inserted value have specific role name
        val response: String = res.response.contentAsString
        val id: Int = JsonPath.parse(response).read("$.id")
        mvc.perform(MockMvcRequestBuilders.get("$uri/{id}", id))
            .andExpect(jsonPath("$.roles[*].name").value(Matchers.hasItem(PrimaryRoles.WORKER)))
            .andExpect(jsonPath("$.roles[*].name").value(Matchers.hasItem(PrimaryRoles.EMPLOYEE)))

        return Json.decodeFromString<RecID>(res.response.contentAsString)
    }

    @Test
    @Transactional
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER])
    fun `delete worker test`() {
        val newRecId = insertTravolta()

        mvc.perform(
            MockMvcRequestBuilders.delete("$uri/{id}", newRecId.asInt())
        ).andExpect(status().isOk)

        mvc.perform(
            MockMvcRequestBuilders.get("$uri/{id}", newRecId.asInt())
        ).andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER])
    fun `insert one test`() {
        insertTravolta()
    }

    @Test
    @Transactional
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER])
    fun `change worker data`() {
        val annaId = 7
        var change = WorkerChangeReq(
            isManagerOf = listOf(1, 2),
            name = "Zofia"
        )
        mvc.perform(
            MockMvcRequestBuilders.put("$uri/{id}", annaId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.encodeToString(change))
        ).andExpect(status().isOk)
        mvc.perform(
            MockMvcRequestBuilders.get("$uri/{id}", annaId)
        ).andExpectAll(
            status().isOk,
            jsonPath("$.name").value("Zofia"),
            jsonPath("$.surname").value("Nadstawna-Naświetlarnia"),
            jsonPath("$.isManagerOf.*").value(Matchers.hasSize<List<RoleResp>>(2)),
            jsonPath("$.isManagerOf[*].name").value(Matchers.hasItem("Introligatornia")),
            jsonPath("$.roles[*].name").value(Matchers.hasItem(PrimaryRoles.WORKFLOW_STAGE_MANAGER)),
            jsonPath("$.roles[*].name").value(Matchers.hasItem(PrimaryRoles.EMPLOYEE)),
            jsonPath("$.roles[*].name").value(Matchers.hasItem(PrimaryRoles.WORKER))
        )

        change = WorkerChangeReq(
            isManagerOf = listOf(1, 2),
            surname = "Nadstawna-Naświetlarnia"
        )

        mvc.perform(
            MockMvcRequestBuilders.put("$uri/{id}", annaId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.encodeToString(change))
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.changed").value(false))
    }
}