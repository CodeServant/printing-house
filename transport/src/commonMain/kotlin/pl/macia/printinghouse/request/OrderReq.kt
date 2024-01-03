package pl.macia.printinghouse.request

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime

@Serializable
data class OrderReq(
    val name: String,
    val comment: String?,
    val withdrawalDate: LocalDateTime?,
    val completionDate: LocalDateTime?,
    val designsNumberForSheet: Int,
    val checked: Boolean,
    val towerCut: Boolean,
    val folding: Boolean,
    val realizationDate: LocalDateTime,
    val creationDate: LocalDateTime,
    val pages: Int,
    val paperOrderTypes: List<PaperOrderTypeReq>,
    val orderEnoblings: List<OrderEnoblingReq>,
    val imageUrl: String?,
    val binderyId: Int,
    val salesmanId: Int,
    val workflowStageStops: List<WorkflowStageStopReq>,
    val bindingFormId: Int,
    val calculationCard: CalculationCardReq?,
    val netSize: SizeReq,
    val clientId: Int
)