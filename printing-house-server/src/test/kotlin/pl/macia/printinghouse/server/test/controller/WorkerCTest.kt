package pl.macia.printinghouse.server.test.controller

import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
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
import pl.macia.printinghouse.response.WorkerResp
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.PrintingHouseServerApplication

/**
 * Constants defined to reduce redundancy in tested code. Constants defined in controllers are not defined here because api may change.
 */
object Paths {
    const val WORKERS = "workers"
    const val CONTEXT = "api"
}

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@WebAppConfiguration
internal class WorkerCTest {
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @BeforeEach
    fun beforeEach() {
        mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
            .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
            .build()
    }

    @Test
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER])
    fun `get all workers test`() {
        mvc.perform(MockMvcRequestBuilders.get("/${Paths.CONTEXT}/${Paths.WORKERS}"))
            .andExpect { status().isOk }
            .andExpectAll (
                jsonPath("$[1].name").value("Jiliusz"),
                jsonPath("$[1].id").value(3),
                jsonPath("$.*").value(Matchers.hasSize<List<WorkerResp>>(4))
            )
    }

    @Test
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER])
    fun `find one test`() {
        val uri = "/${Paths.CONTEXT}/${Paths.WORKERS}/{id}"
        fun perform(id: Int) = mvc.perform(MockMvcRequestBuilders.get(uri, id))
        perform(3)
            .andExpect(status().isOk)
            .andExpectAll(
                jsonPath("$.name").value("Jiliusz"),
                jsonPath("$.id").value(3)
            )
        perform(1)
            .andExpect(status().isNotFound)
    }
}