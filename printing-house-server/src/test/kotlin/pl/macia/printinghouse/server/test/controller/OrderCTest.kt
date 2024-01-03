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
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.PrintingHouseServerApplication
import java.time.LocalDateTime

@SpringBootTest(classes = [PrintingHouseServerApplication::class])
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderCTest {
    private lateinit var standardTest: StdCTest

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private val uri = "/${Paths.CONTEXT}/${Paths.ORDERS}"

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
    fun `get one by id`() {
        standardTest.checkFindOneById(
            1,
            jsonPath("$.id").value(1),
            jsonPath("$.name").value("gorzelska"),
            jsonPath("$.comment").doesNotExist(),
            jsonPath("$.withdrawalDate").doesNotExist(),
            jsonPath("$.completionDate").doesNotExist(),
            jsonPath("$.designsNumberForSheet").value(4),
            jsonPath("$.checked").value(true),
            jsonPath("$.towerCut").value(true),
            jsonPath("$.folding").value(false),
            jsonPath("$.realizationDate").value(LocalDateTime.parse("2022-10-26T00:00").toString()),
            jsonPath("$.creationDate").value(LocalDateTime.parse("2022-10-21T00:00").toString()),
            jsonPath("$.pages").value(8),
            jsonPath("$.paperOrderTypes").value(Matchers.hasSize<Int>(2)),
            jsonPath("$.paperOrderTypes[*].grammage").value(
                Matchers.hasItems(70.0, 90.0)
            ),
            jsonPath("$.paperOrderTypes[*].circulation").value(
                Matchers.hasItems(20000, 2000)
            ),
            jsonPath("$.paperOrderTypes[*].paperType.id").value(
                Matchers.hasItems(2, 3)
            ),
            jsonPath("$.orderEnoblings").value(Matchers.hasSize<Int>(2)),
            jsonPath("$.orderEnoblings[*].id").value(
                Matchers.hasItems(1, 2)
            ),
            jsonPath("$.orderEnoblings[*].annotation").value(
                Matchers.hasItems("zróbcie jeszcze szpagat", null)
            ),
            jsonPath("$.imageUrl").value(Matchers.startsWith("https://viewer.diagrams.net/?tags=%7B%7D&highlight")),
            jsonPath("$.bindery.id").value(3),
            jsonPath("$.salesman.id").value(1),
            jsonPath("$.workflowStageStops").value(Matchers.hasSize<Int>(2)),
            jsonPath("$.workflowStageStops[*].wfssId").value(Matchers.hasItems(1, 2)),
            jsonPath("$.bindingForm.id").value(3),
            jsonPath("$.calculationCard.id").value(1),
            jsonPath("$.netSize.id").value(7),
            jsonPath("$.client.clientId").value(3),
            jsonPath("$.client.companyId").value(1)
        )
    }
}