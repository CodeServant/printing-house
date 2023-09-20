package pl.macia.printinghouse.server.bmodel

import java.math.BigDecimal
import java.time.LocalDateTime

sealed interface Order {
    var orderid: Int?
    var name: String
    var comment: String?
    var withdrawalDate: LocalDateTime?
    var completionDate: LocalDateTime?
    var designsNumberForSheet: Int
    var checked: Boolean
    var imageComment: String?
    var towerCut: Boolean
    var folding: Boolean

    /**
     * Expected realization date.
     */
    var realizationDate: LocalDateTime
    var caretionDate: LocalDateTime
    var pages: Int
    val paperOrderTypes: MutableList<PaperOrderType>
    val orderEnoblings: MutableList<OrderEnobling>
    var url: URL?
    var bindery: Bindery
    var salesman: Salesman
    val workflowStageStops: MutableList<WorkflowStageStop>
    var bindingForm: BindingForm
    var calculationCard: CalculationCard?
    var netSize: Size
    var client: Client
    fun addWorkflowStageStop(
        comment: String?,
        lastWorkflowStage: Boolean,
        assignTime: LocalDateTime?,
        createTime: LocalDateTime,
        worker: Worker?,
        workflowStage: WorkflowStage
    ): WorkflowStageStop

    fun addOrderEnobling(
        annotation: String?,
        enobling: Enobling,
        bindery: Bindery
    ): OrderEnobling

    fun addPaperOrderType(
        grammage: Number,
        stockCirculation: Int,
        sheetNumber: Int,
        comment: String?,
        circulation: Int,
        platesQuantityForPrinter: Int,
        paperType: PaperType,
        printer: Printer,
        colouring: Colouring,
        impositionType: ImpositionType,
        size: Size,
        productionSize: Size
    ): PaperOrderType

    fun setCalculationCard(
        transport: BigDecimal,
        otherCosts: BigDecimal,
        enoblingCost: BigDecimal,
        bindingCost: BigDecimal
    ): CalculationCard
}