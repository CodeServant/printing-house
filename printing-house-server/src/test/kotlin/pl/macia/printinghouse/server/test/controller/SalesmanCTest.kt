package pl.macia.printinghouse.server.test.controller

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import pl.macia.printinghouse.request.SalesmanChangeReq
import pl.macia.printinghouse.request.SalesmanReq
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.response.WorkerResp
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.PrintingHouseServerApplication

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@WebAppConfiguration
class SalesmanCTest {
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private val uri = "/${Paths.CONTEXT}/${Paths.SALESMANS}"

    @BeforeEach
    fun beforeEach() {
        mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
            .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
            .build()
    }

    @Test
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER])
    fun `find hired salesmans`() {
        mvc.perform(MockMvcRequestBuilders.get(uri))
            .andExpect { MockMvcResultMatchers.status().isOk }
            .andExpectAll(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Jan"),
                MockMvcResultMatchers.jsonPath("$[0].id").value(1),
                MockMvcResultMatchers.jsonPath("$.*").value(Matchers.hasSize<List<WorkerResp>>(1))
            )
    }

    @Test
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER])
    fun `find one test`() {
        fun perform(id: Int) = mvc.perform(MockMvcRequestBuilders.get("$uri/{id}", id))
        perform(1)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpectAll(
                MockMvcResultMatchers.jsonPath("$.name").value("Jan"),
                MockMvcResultMatchers.jsonPath("$.email").value("evilcorp@example.com"),
                MockMvcResultMatchers.jsonPath("$.roles[0].name").value("SALESMAN"),
                MockMvcResultMatchers.jsonPath("$.id").value(1)
            )
        perform(2)
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    fun dummyJackson(pesel: String, email: String): SalesmanReq {
        return SalesmanReq(
            roles = listOf(),
            employed = true,
            activeAccount = true,
            password = "123",
            email = email,
            psudoPESEL = pesel,
            surname = "Travolta",
            name = "John"
        )
    }

    /**
     * Inserts and checks if property interacted worker travolta.
     */
    fun insertJackson(): RecID {
        val workerReq = dummyJackson("72827641678", "jacksonEmail@example.com")
        val res = mvc.perform(
            MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON)
                .content(Json.encodeToString(workerReq))
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        return Json.decodeFromString<RecID>(res.response.contentAsString)
    }

    @Test
    @Transactional
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER])
    fun `insert one test`() {
        insertJackson()
    }

    @Test
    @Transactional
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER])
    fun `delete worker test`() {
        val newRecId = insertJackson()

        mvc.perform(
            MockMvcRequestBuilders.delete("$uri/{id}", newRecId.asInt())
        ).andExpect(MockMvcResultMatchers.status().isOk)

        mvc.perform(
            MockMvcRequestBuilders.get("$uri/{id}", newRecId.asInt())
        ).andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    @Transactional
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER])
    fun `change salesman data`() {
        // this code is similar to WorkerCTest change test and maybe can be refactored
        val janId = 1
        var change = SalesmanChangeReq(
            name = "Zofia"
        )
        mvc.perform(
            MockMvcRequestBuilders.put("$uri/{id}", janId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.encodeToString(change))
        ).andExpect(MockMvcResultMatchers.status().isOk)
        mvc.perform(
            MockMvcRequestBuilders.get("$uri/{id}", janId)
        ).andExpectAll(
            MockMvcResultMatchers.status().isOk,
            MockMvcResultMatchers.jsonPath("$.name").value("Zofia"),
            MockMvcResultMatchers.jsonPath("$.surname").value("Kowalski-Salesman")
        )

        change = SalesmanChangeReq(
            surname = "Kowalski-Salesman"
        )

        mvc.perform(
            MockMvcRequestBuilders.put("$uri/{id}", janId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.encodeToString(change))
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.changed").value(false))
    }
}