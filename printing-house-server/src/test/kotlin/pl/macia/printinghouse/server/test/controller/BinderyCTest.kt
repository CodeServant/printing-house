package pl.macia.printinghouse.server.test.controller

import org.hamcrest.Matchers
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
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
}