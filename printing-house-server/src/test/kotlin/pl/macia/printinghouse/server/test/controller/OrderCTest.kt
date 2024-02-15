package pl.macia.printinghouse.server.test.controller

import kotlinx.datetime.toKotlinLocalDateTime
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
import pl.macia.printinghouse.request.*
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

    @Test
    @WithMockUser("jankowa@wp.pl", authorities = [PrimaryRoles.MANAGER])
    fun `insert new one`() {
        val new = OrderReq(
            name = "new order",
            comment = "this is test order",
            withdrawalDate = null,
            completionDate = LocalDateTime.parse("2024-01-13T00:00").toKotlinLocalDateTime(),
            designsNumberForSheet = 4,
            checked = false,
            towerCut = true,
            folding = false,
            realizationDate = LocalDateTime.parse("2024-01-12T00:00").toKotlinLocalDateTime(),
            creationDate = LocalDateTime.parse("2024-01-08T00:00").toKotlinLocalDateTime(),
            pages = 10,
            paperOrderTypes = listOf(
                PaperOrderTypeReq(
                    grammage = 70.0,
                    stockCirculation = 70,
                    sheetNumber = 1,
                    comment = "some comment on paper type",
                    circulation = 20_000,
                    platesQuantityForPrinter = 1,
                    paperTypeId = 2,
                    printerId = 1,
                    colouringId = 4,
                    impositionTypeId = 1,
                    size = SizeReq(
                        heigth = 594.0,
                        width = 420.0
                    ),
                    productionSize = SizeReq(
                        heigth = 610.0,
                        width = 430.0
                    )
                )
            ),
            orderEnoblings = listOf(
                OrderEnoblingReq(
                    annotation = "some annotation",
                    enoblingId = 4,
                    binderyId = 2
                )
            ),
            imageUrl = ImageReq(
                url = "https://viewer.diagrams.net/?tags=%7B%7D&highlight=0000ff&edit=_blank&layers=1&nav=1&title=Diagram%20bez%20tytu%C5%82u.drawio#RzZXRjqMgFIafhstNFGrrXk51djoXk0zSSfaayhklRTCIo%2FbpFyrWWneSnWST6ZXwn8MPfAcBkaTsnjStihfFQCAcsA6RFGEcrsjKfpzSD8omxoOQa8580iTs%2BQm8GHi14QzqWaJRShhezcVMSQmZmWlUa9XO096VmM9a0RwWwj6jYqn%2B5swUgxrjzaTvgOfFOHO4%2FjlESjom%2B53UBWWqvZLIIyKJVsoMrbJLQDh4I5dh3K9PopeFaZDmXwa8vqVV%2F3J4o1H6%2FBC0h0x0ux%2Fe5YOKxm%2FYL9b0IwGtGsnAmQSIbNuCG9hXNHPR1tbcaoUphe2FtuntQBvoPl1neNm9PTagSjC6tynjgNgD8ycGb3y%2FnfiHI9Tiiv3aa9SXPL9YT1Rsw4P5AiR8f5BIeG%2BQyP1Buj1JZPXdkFb3B%2Bn2JH0%2FpGgBqdKn%2FoiSCMXY3aFu9bo%2FNRKOC3wWhJkzooLn0rYzCwS0FRwubm%2F4Bx8oOWNu%2BFZDzU%2F0cLZy8CvFpTnvLtqiKHVejVH18EY569podYRECWV9U6mkc3nnQtxI%2F6FMmETzHz6Il2UifykT%2BXqZbHd6ls6xq8edPP4B",
                comment = "my schetch"
            ),
            binderyId = 3,
            salesmanId = 1,
            workflowDirGraphId = 1,
            bindingFormId = 3,
            calculationCard = CalculationCardReq(
                transport = "1516.00",
                otherCosts = "1251.00",
                enoblingCost = "1245.00",
                bindingCost = "1000.00",
                printCosts = listOf(
                    PrintCostReq(
                        printCost = "12.00",
                        matrixCost = "13.00",
                        printerId = 1
                    ),
                    PrintCostReq(
                        printCost = "13.00",
                        matrixCost = "14.00",
                        printerId = 1
                    )
                ),
            ),
            netSize = SizeReq(
                width = 205.0,
                heigth = 295.0
            ),
            clientId = 3
        )
        standardTest.checkInsertOneObj(
            Json.encodeToString(new),
            "id",
            jsonPath("$.id").isNumber,
            jsonPath("$.name").value("new order"),
            jsonPath("$.comment").value("this is test order"),
            jsonPath("$.withdrawalDate").doesNotExist(),
            jsonPath("$.completionDate").value("2024-01-13T00:00"),
            jsonPath("$.designsNumberForSheet").value(4),
            jsonPath("$.checked").value(false),
            jsonPath("$.towerCut").value(true),
            jsonPath("$.folding").value(false),
            jsonPath("$.realizationDate").value(LocalDateTime.parse("2024-01-12T00:00").toString()),
            jsonPath("$.creationDate").value(LocalDateTime.parse("2024-01-08T00:00").toString()),
            jsonPath("$.pages").value(10),
            jsonPath("$.paperOrderTypes").value(Matchers.hasSize<Int>(1)),
            jsonPath("$.paperOrderTypes[*].grammage").value(
                Matchers.hasItems(70.0)
            ),
            jsonPath("$.paperOrderTypes[*].circulation").value(
                Matchers.hasItems(20_000)
            ),
            jsonPath("$.paperOrderTypes[*].paperType.id").value(
                Matchers.hasItems(2)
            ),
            jsonPath("$.orderEnoblings").value(Matchers.hasSize<Int>(1)),
            jsonPath("$.orderEnoblings[*].enobling.id").value(
                Matchers.hasItems(4)
            ),
            jsonPath("$.orderEnoblings[*].bindery.id").value(
                Matchers.hasItems(2)
            ),
            jsonPath("$.orderEnoblings[*].annotation").value(
                Matchers.hasItems("some annotation")
            ),
            jsonPath("$.imageUrl").value(Matchers.startsWith("https://viewer.diagrams.net/?tags=%7B%7D&highlight")),
            jsonPath("$.bindery.id").value(3),
            jsonPath("$.salesman.id").value(1),
            jsonPath("$.workflowStageStops").value(Matchers.hasSize<Int>(2)),
            jsonPath("$.bindingForm.id").value(3),
            jsonPath("$.calculationCard.transport").value("1516.00"),
            jsonPath("$.netSize.id").value(7),
            jsonPath("$.client.clientId").value(3),
            jsonPath("$.client.companyId").value(1)
        )
    }

    @Test
    @WithMockUser("janworker@example.pl", authorities = [PrimaryRoles.MANAGER])
    fun getOrdersForWorkerTest() {
        TODO("Not yet implemented")
    }
}