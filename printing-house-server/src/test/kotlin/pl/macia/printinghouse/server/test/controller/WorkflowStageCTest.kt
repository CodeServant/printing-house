package pl.macia.printinghouse.server.test.controller

import jakarta.transaction.Transactional
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hamcrest.Matchers
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.web.context.WebApplicationContext
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import pl.macia.printinghouse.request.WorkflowStageChangeReq
import pl.macia.printinghouse.request.WorkflowStageReq
import pl.macia.printinghouse.response.PersonsIdentityResp
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
            jsonPath("$.managers[0].name").value("Robert"),
            jsonPath("$.managers[0].id").value(4),
            jsonPath("$.name").value("Naświetlarnia")
        )
    }

    fun dummyWorkflowStage(name: String, managers: List<Int>): WorkflowStageReq {
        return WorkflowStageReq(managers, name)
    }

    fun insertWorkflowStageChecks(name: String, managers: List<Int> = listOf(3, 4)): RecID {
        val wfReq = dummyWorkflowStage(name, managers)

        return standardTest.checkInsertOneObj(
            Json.encodeToString(wfReq),
            idJName = "id",
            jsonPath("$.name").value(name),
            jsonPath("$.managers[*]").value(Matchers.hasSize<PersonsIdentityResp>(2))
        )
    }

    @Test
    @Transactional
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER, PrimaryRoles.SALESMAN, PrimaryRoles.EMPLOYEE])
    fun `insert one test`() {
        val workers = listOf(4, 7)
        val workerURL = "/${Paths.CONTEXT}/${Paths.WORKERS}/{id}"
        insertWorkflowStageChecks("insertOneTestController", workers)
        val nonManagerId = 7
        standardTest.mvc.perform(MockMvcRequestBuilders.get(workerURL, nonManagerId))
            .andExpectAll(
                status().isOk,
                jsonPath("$.isManagerOf[*].name").value(Matchers.hasItems("insertOneTestController")),
                jsonPath("$.roles[*].name").value(
                    Matchers.hasItems(
                        PrimaryRoles.WORKFLOW_STAGE_MANAGER,
                        PrimaryRoles.EMPLOYEE,
                        PrimaryRoles.WORKER
                    )
                )
            )
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