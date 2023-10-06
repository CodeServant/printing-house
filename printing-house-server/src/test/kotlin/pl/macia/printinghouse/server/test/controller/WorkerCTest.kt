package pl.macia.printinghouse.server.test.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.hamcrest.Matchers
import pl.macia.printinghouse.response.WorkerResp

/**
 * Constants defined to reduce redundancy in tested code. Constants defined in controllers are not defined here because api may change.
 */
object Paths {
    const val WORKERS = "workers"
    const val CONTEXT = "api"
}

@SpringBootTest
@WebAppConfiguration
internal class WorkerCTest {
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @BeforeEach
    fun beforeEach() {
        mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build()
    }

    @Test
    fun `example test`() {
        mvc.perform(MockMvcRequestBuilders.get("/${Paths.CONTEXT}/${Paths.WORKERS}"))
            .andDo(::print)
            .andExpect(status().isOk)
            .andExpectAll(
                jsonPath("$[1].name").value("Jiliusz"),
                jsonPath("$[1].id").value(3),
                jsonPath("$.*").value(Matchers.hasSize<List<WorkerResp>>(4))
            )
    }
}