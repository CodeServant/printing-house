package pl.macia.printinghouse.server.test.controller

import com.jayway.jsonpath.JsonPath
import jakarta.transaction.Transactional
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import pl.macia.printinghouse.request.BinderyChangeReq
import pl.macia.printinghouse.request.BinderyReq
import pl.macia.printinghouse.response.RecID

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@WebAppConfiguration
class BinderyCTest {
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private val uri = "/${Paths.CONTEXT}/${Paths.BINDERIES}"

    @BeforeEach
    fun beforeEach() {
        mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
            .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
            .build()
    }

    @Test
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER])
    fun `get all binderies test`() {
        mvc.perform(MockMvcRequestBuilders.get(uri))
            .andExpect { status().isOk }
            .andExpectAll(
                jsonPath("$[*].name").value(Matchers.hasItem("A1")),
                jsonPath("$[*].name").value(Matchers.hasItem("A2"))
            )
    }

    @Test
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER])
    fun `get one by id`() {
        mvc.perform(MockMvcRequestBuilders.get("$uri/{id}", 2))
            .andExpect { status().isOk }
            .andExpectAll(
                jsonPath("$.id").value(2),
                jsonPath("$.name").value("A2")
            )
        mvc.perform(MockMvcRequestBuilders.get("$uri/{id}", 1111))
            .andExpect { status().isNotFound }
    }

    fun dummyBindery(name: String): BinderyReq {
        return BinderyReq(name)
    }

    fun insertBindery(name: String): RecID {
        val bindReq = dummyBindery(name)

        val res = mvc.perform(
            MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON)
                .content(Json.encodeToString(bindReq))
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
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER])
    fun `insert one test`() {
        insertBindery("insertOneTestController")
    }

    @Test
    @Transactional
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER])
    fun `delete bindery test`() {
        val newRecId = insertBindery("bindery1")

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
    fun `change bindery data`() {
        val binderyId = 1
        val change = BinderyChangeReq(
            name = "A66"
        )
        mvc.perform(
            MockMvcRequestBuilders.put("$uri/{id}", binderyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.encodeToString(change))
        ).andExpectAll(
            status().isOk,
            jsonPath("$.changed").value(true)
        )
        mvc.perform(
            MockMvcRequestBuilders.get("$uri/{id}", binderyId)
        ).andExpectAll(
            status().isOk,
            jsonPath("$.name").value("A66")
        )

        mvc.perform(
            MockMvcRequestBuilders.put("$uri/{id}", binderyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.encodeToString(change))
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.changed").value(false))
    }
}