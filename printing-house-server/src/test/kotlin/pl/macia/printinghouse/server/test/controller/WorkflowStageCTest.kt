package pl.macia.printinghouse.server.test.controller

import jakarta.transaction.Transactional
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.web.context.WebApplicationContext
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import pl.macia.printinghouse.request.WorkflowStageChangeReq
import pl.macia.printinghouse.request.WorkflowStageReq
import pl.macia.printinghouse.response.RecID

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
class WorkflowStageCTest {
    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private val uri = "/${Paths.CONTEXT}/${Paths.WORKFLOW_STAGES}"

    private lateinit var standardTest: StdCTest

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
    fun `get all binderies test`() {
        standardTest.checkGetAllFromPath(
            jsonPath("$[*].name").value(Matchers.hasItems("Handlowiec", "Introligatornia"))
        )
    }

    @Test
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.EMPLOYEE])
    fun `get one by id`() {
        standardTest.checkFindOneById(
            2,
            jsonPath("$.id").value(2),
            jsonPath("$.name").value("Naświetlarnia")
        )
    }

    fun dummyWorkflowStage(name: String): WorkflowStageReq {
        return WorkflowStageReq(name)
    }

    fun insertWorkflowStageChecks(name: String): RecID {
        val wfReq = dummyWorkflowStage(name)

        return standardTest.checkInsertOneObj(
            Json.encodeToString(wfReq),
            idJName = "id",
            jsonPath("$.name").value(name)
        )
    }

    @Test
    @Transactional
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER, PrimaryRoles.SALESMAN])
    fun `insert one test`() {
        insertWorkflowStageChecks("insertOneTestController")
    }

    @Test
    @Transactional
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER])
    fun `delete workflow stage test`() {
        standardTest.checkDeleteObjTest(
            insertWorkflowStageChecks("insertedValue")
        )
    }

    @Test
    @Transactional
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER])
    fun `change workflow stage data`() {
        val workflowStageId = 1
        val change = WorkflowStageChangeReq(
            name = "NameChanged"
        )
        standardTest.checkChangeObjTest(
            workflowStageId,
            Json.encodeToString(change),
            jsonPath("$.name").value("NameChanged")
        )
    }
}