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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import pl.macia.printinghouse.request.BindingFormReq
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.response.WorkerResp
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.PrintingHouseServerApplication

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@WebAppConfiguration
class BindingFormCTest {
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private val uri = "/${Paths.CONTEXT}/${Paths.BINDING_FORMS}"

    @BeforeEach
    fun beforeEach() {
        // todo this line is every test of controller
        mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
            .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
            .build()
    }

    @Test
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.SALESMAN])
    fun `find all binding forms`() {
        mvc.perform(MockMvcRequestBuilders.get(uri))
            .andExpect { status().isOk }
            .andExpectAll(
                jsonPath("$[*].name").value(Matchers.hasItems("Folia", "Karton", "Papier")),
                jsonPath("$.*").value(Matchers.hasSize<List<WorkerResp>>(4))
            )
    }

    @Test
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER])
    fun `get one by id`() {
        mvc.perform(MockMvcRequestBuilders.get("$uri/{id}", 2))
            .andExpect { status().isOk }
            .andExpectAll(
                jsonPath("$.id").value(2),
                jsonPath("$.name").value("Folia")
            )
        mvc.perform(MockMvcRequestBuilders.get("$uri/{id}", 1111))
            .andExpect { status().isNotFound }
    }

    fun dummyBindingForm(name: String): BindingFormReq {
        return BindingFormReq(name)
    }

    fun insertBindingForm(name: String): RecID {
        val bindFormReq = dummyBindingForm(name)

        val res = mvc.perform(
            MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON)
                .content(Json.encodeToString(bindFormReq))
        ).andExpect(status().isOk)
            .andReturn()


        val response: String = res.response.contentAsString
        val id: Int = JsonPath.parse(response).read("$.id")
        mvc.perform(MockMvcRequestBuilders.get("$uri/{id}", id))
            .andExpect(jsonPath("$.name").value(name))
            .andExpect(jsonPath("$.id").value(id))

        return Json.decodeFromString<RecID>(res.response.contentAsString)
    }

    @Test
    @Transactional
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER, PrimaryRoles.SALESMAN])
    fun `insert one test`() {
        insertBindingForm("insertOneTestController")
    }
}