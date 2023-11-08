package pl.macia.printinghouse.server.test.controller

import jakarta.transaction.Transactional
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
import pl.macia.printinghouse.request.WorkflowEdgeReq
import pl.macia.printinghouse.request.WorkflowGraphReq
import pl.macia.printinghouse.response.RecID
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

    fun dummyWorkflowGraphReq(name: String): WorkflowGraphReq {
        return WorkflowGraphReq(
            name = name,
            comment = "some",
            edges = listOf(
                WorkflowEdgeReq(1, 2),
                WorkflowEdgeReq(2, 3)
            )
        )
    }

    fun insertWorkflowGraph(name: String): RecID {
        val workflowGraphReq = dummyWorkflowGraphReq(name)

        return standardTest.checkInsertOneObj(
            Json.encodeToString(workflowGraphReq),
            "id",
            jsonPath("$.name").value(name),
            jsonPath("$.comment").value("some"),
            jsonPath("$.edges").value(Matchers.hasSize<WorkflowStageRespEmb>(2)),
            jsonPath("$.edges[0].v1.name").value("Introligatornia"),
            jsonPath("$.edges[0].v2.name").value("Naświetlarnia"),
            jsonPath("$.edges[1].v2.name").value("Handlowiec"),
            jsonPath("$.edges[*].['v1','v2'].id").value(Matchers.hasSize<Int>(4)),

            )
    }

    @Test
    @Transactional
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER])
    fun `insert one test`() {
        insertWorkflowGraph("new schema automated test")
    }
}