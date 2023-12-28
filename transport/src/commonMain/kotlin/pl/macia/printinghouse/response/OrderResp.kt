package pl.macia.printinghouse.response

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime

@Serializable
data class OrderResp(
    val id: Int,
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
    val paperOrderTypes: List<PaperOrderTypeResp>,
    val orderEnoblings: List<OrderEnoblingResp>,
    val imageUrl: String?,
    val bindery: BinderyResp,
    val salesman: SalesmanResp,
    val workflowStageStops: List<WorkflowStageStopResp>,
    val bindingForm: BindingFormResp,
    val calculationCard: CalculationCardRespEmb?,
    val netSize: SizeResp,
    val client: ClientResp
)
