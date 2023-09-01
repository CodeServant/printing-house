package pl.macia.printinghouse.server.bmodel

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
    var realizationDate: LocalDateTime
    var caretionDate: LocalDateTime
    var pages: Int
    var paperOrderTypes: List<PaperOrderType>
    var orderEnoblings: List<OrderEnobling>
    var url: URL?
    var bindery: Bindery
    var salesman: Salesman
    var workflowStageStops: List<WorkflowStageStop>
    var bindingForm: BindingForm
    var calculationCard: CalculationCard?
    var netSize: Size
}