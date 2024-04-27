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
import pl.macia.printinghouse.request.BinderyChangeReq
import pl.macia.printinghouse.request.BinderyReq
import pl.macia.printinghouse.response.RecID

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
class BinderyCTest {
    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private val uri = "/${Paths.CONTEXT}/${Paths.BINDERIES}"

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
    @WithMockUser("juliusz@example.com", authorities = [PrimaryRoles.MANAGER])
    fun `get all binderies test`() {
        standardTest.checkGetAllFromPath(
            jsonPath("$[*].name").value(Matchers.hasItem("A1")),
            jsonPath("$[*].name").value(Matchers.hasItem("A2"))
        )
    }

    @Test
    @WithMockUser("juliusz@example.com", authorities = [PrimaryRoles.MANAGER])
    fun `get one by id`() {
        standardTest.checkFindOneById(
            2,
            jsonPath("$.id").value(2),
            jsonPath("$.name").value("A2")
        )
    }

    fun dummyBindery(name: String): BinderyReq {
        return BinderyReq(name)
    }

    fun insertBindery(name: String): RecID {
        val bindReq = dummyBindery(name)

        return standardTest.checkInsertOneObj(
            Json.encodeToString(bindReq),
            idJName = "id",
            jsonPath("$.name").value(name)
        )
    }

    @Test
    @Transactional
    @WithMockUser("juliusz@example.com", authorities = [PrimaryRoles.MANAGER])
    fun `insert one test`() {
        insertBindery("insertOneTestController")
    }

    @Test
    @Transactional
    @WithMockUser("juliusz@example.com", authorities = [PrimaryRoles.MANAGER])
    fun `delete bindery test`() {
        standardTest.checkDeleteObjTest(
            insertBindery("bindery1")
        )
    }

    @Test
    @Transactional
    @WithMockUser("juliusz@example.com", authorities = [PrimaryRoles.MANAGER])
    fun `change bindery data`() {
        val binderyId = 1
        val change = BinderyChangeReq(
            name = "A66"
        )
        standardTest.checkChangeObjTest(
            binderyId,
            Json.encodeToString(change),
            jsonPath("$.name").value("A66")
        )
    }
}