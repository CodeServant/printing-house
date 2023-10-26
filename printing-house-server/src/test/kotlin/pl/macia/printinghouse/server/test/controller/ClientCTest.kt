package pl.macia.printinghouse.server.test.controller

import jakarta.transaction.Transactional
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
import pl.macia.printinghouse.request.*
import pl.macia.printinghouse.request.ClientReq
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.PrintingHouseServerApplication

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClientCTest {
    private lateinit var standardTest: StdCTest

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private val uri = "/${Paths.CONTEXT}/${Paths.CLIENTS}"

    @BeforeAll
    fun setUp() {
        standardTest = StdCTest(uri, webApplicationContext)
    }

    @BeforeEach
    fun beforeEach() {
        standardTest.beforeEach()
    }

    @Test
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.SALESMAN])
    fun `find one client test`() {
        standardTest.checkFindOneById(
            1,
            jsonPath("$.type").value("IndividualClient"),
            jsonPath("$.phoneNumber").value("984324654"),
            jsonPath("$.clientId").value(1),
            jsonPath("$.personId").value(6),
            jsonPath("$.companyId").doesNotExist(),
            jsonPath("$.nip").doesNotExist()
        )
        standardTest.checkFindOneById(
            3,
            jsonPath("$.type").value("CompanyClient"),
            jsonPath("$.phoneNumber").value("152358752"),
            jsonPath("$.name").value("evil corp inc."),
            jsonPath("$.clientId").value(3),
            jsonPath("$.companyId").value(1),
            jsonPath("$.personId").doesNotExist(),
            jsonPath("$.surname").doesNotExist()
        )
    }

    val dummyIndCliReq = IndividualClientReq(
        phoneNumber = "949257122",
        psudoPESEL = "25478541254",
        surname = "Winnicka",
        name = "Magda"
    )

    val dummyCompCliReq = CompanyClientReq(
        email = "653753625@example.com",
        nip = "9669696969",
        name = "FaceNotebook",
        phoneNumber = "154735326"
    )

    @Test
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.SALESMAN])
    @Transactional
    fun `insert one test`() {
        standardTest.checkInsertOneObj(
            Json.encodeToString(
                dummyIndCliReq as ClientReq
            ), "clientId",
            jsonPath("$.email").doesNotExist(),
            jsonPath("$.type").value("IndividualClient"),
            jsonPath("$.name").value("Magda"),
            jsonPath("$.surname").value("Winnicka"),
            jsonPath("$.phoneNumber").value("949257122")
        )
        standardTest.checkInsertOneObj(
            Json.encodeToString(
                dummyCompCliReq as ClientReq
            ), "clientId",
            jsonPath("$.email").value("653753625@example.com"),
            jsonPath("$.type").value("CompanyClient"),
            jsonPath("$.name").value("FaceNotebook"),
            jsonPath("$.phoneNumber").value("154735326"),
            jsonPath("$.nip").value("9669696969")
        )
    }

    @Test
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.SALESMAN])
    @Transactional
    fun `change individual client test`() {
        val redId = standardTest.checkInsertOneObj(
            Json.encodeToString(
                dummyIndCliReq as ClientReq
            ), "clientId"
        )
        val changeReq = IndividualClientChangeReq(
            email = "magda15466@example.com",
            nullingRest = true,
            surname = "Vinnitska"
        )
        standardTest.checkChangeObjTest(
            redId.asInt(),
            Json.encodeToString(changeReq as ClientChangeReq),
            jsonPath("$.email").value("magda15466@example.com"),
            jsonPath("$.phoneNumber").doesNotExist(),
            jsonPath("$.surname").value("Vinnitska"),
            jsonPath("$.name").value("Magda"),
        )
    }

    @Test
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.SALESMAN])
    @Transactional
    fun `change company client test`() {
        val redId = standardTest.checkInsertOneObj(
            Json.encodeToString(
                dummyCompCliReq as ClientReq
            ), "clientId"
        )
        val changeReq = CompanyClientChangeReq(
            email = "faceCorp@example.com",
            nullingRest = true,
            name = "FaceNotepad"
        )
        standardTest.checkChangeObjTest(
            redId.asInt(),
            Json.encodeToString(changeReq as ClientChangeReq),
            jsonPath("$.email").value("faceCorp@example.com"),
            jsonPath("$.phoneNumber").doesNotExist(),
            jsonPath("$.name").value("FaceNotepad"),
            jsonPath("$.nip").value("9669696969"),
        )
    }
}