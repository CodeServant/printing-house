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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.web.context.WebApplicationContext
import pl.macia.printinghouse.response.WorkflowStageRespEmb
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.PrintingHouseServerApplication

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WorkflowGraphCTest {
    private lateinit var standardTest: StdCTest

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private val uri = "/${Paths.CONTEXT}/${Paths.WORKFLOW_GRAPHS}"

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
    fun `find one graph test`() {
        standardTest.checkFindOneById(
            1,
            jsonPath("$.id").value(1),
            jsonPath("$.edges").value(Matchers.hasSize<WorkflowStageRespEmb>(4)),
            jsonPath("$.edges[?(@.edgeId == 2)].v1.id").value(3),
            jsonPath("$.edges[?(@.edgeId == 2)].v1.name").value("Handlowiec"),
            jsonPath("$.edges[?(@.edgeId == 2)].v2.id").value(2),
            jsonPath("$.edges[?(@.edgeId == 2)].v2.name").value("Naświetlarnia"),
        )
    }
}