package pl.macia.printinghouse.server.test.controller

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
}