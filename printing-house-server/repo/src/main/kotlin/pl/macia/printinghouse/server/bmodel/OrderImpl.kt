package pl.macia.printinghouse.server.bmodel

import java.time.LocalDateTime
import pl.macia.printinghouse.server.dto.Order as POrder

internal class OrderImpl(p: POrder) : Order, BusinessBase<POrder>(p) {
    //TODO constructor
    override var orderid: Int? by persistent::id
    override var name: String by persistent::name
    override var comment: String? by persistent::comment
    override var withdrawalDate: LocalDateTime? by persistent::withdrawalDate
    override var completionDate: LocalDateTime? by persistent::completionDate
    override var designsNumberForSheet: Int by persistent::designsNumberForSheet
    override var checked: Boolean by persistent::checked
    override var imageComment: String? by persistent::imageComment
    override var towerCut: Boolean by persistent::towerCut
    override var folding: Boolean by persistent::folding
    override var realizationDate: LocalDateTime by persistent::realizationDate
    override var caretionDate: LocalDateTime by persistent::creationDate
    override var pages: Int by persistent::pages
    override val paperOrderTypes: MutableList<PaperOrderType> = toBizPaperOrderType(persistent.paperOrderTypes)
    override val orderEnoblings: MutableList<OrderEnobling> = toBizOrderEnobling(persistent.orderEnoblings)
    override var url: URL? by delegate(persistent.imageURL, ::URLImpl, URL::class.java)
    override var bindery: Bindery by delegate(persistent.bindery, ::BinderyImpl, Bindery::class.java)
    override var salesman: Salesman by delegate(persistent.supervisor, ::SalesmanImpl, Salesman::class.java)
    override val workflowStageStops: MutableList<WorkflowStageStop> = toBizWorkflowStageStop(persistent.workflowStageStops)
    override var bindingForm: BindingForm by delegate(
        persistent.bindingForm,
        ::BindingFormImpl,
        BindingForm::class.java
    )
    override var calculationCard: CalculationCard?
        get() = TODO("Not yet implemented")
        set(value) {}
    override var netSize: Size by delegate(persistent.netSize, ::SizeImpl, Size::class.java)
}
//TODO constructor function