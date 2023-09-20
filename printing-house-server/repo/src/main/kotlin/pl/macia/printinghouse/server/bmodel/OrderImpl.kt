package pl.macia.printinghouse.server.bmodel

import java.time.LocalDateTime
import pl.macia.printinghouse.server.dto.Order as POrder

internal class OrderImpl(p: POrder) : Order, BusinessBase<POrder>(p) {
    constructor(
        name: String,
        netSize: SizeImpl,
        comment: String?,
        withdrawalDate: LocalDateTime?,
        completionDate: LocalDateTime?,
        designsNumberForSheet: Int,
        checked: Boolean,
        imageComment: String?,
        towerCut: Boolean,
        folding: Boolean,
        client: ClientImpl,
        realizationDate: LocalDateTime,
        caretionDate: LocalDateTime,
        pages: Int,
        url: URLImpl?,
        bindery: BinderyImpl,
        salesman: SalesmanImpl,
        bindingForm: BindingFormImpl
    ) : this(
        POrder(
            name = name,
            netSize = netSize.persistent,
            pages = pages,
            supervisor = salesman.persistent,
            client = client.persistent,
            creationDate = caretionDate,
            realizationDate = realizationDate,
            bindingForm = bindingForm.persistent,
            bindery = bindery.persistent,
            folding = folding,
            towerCut = towerCut,
            imageURL = url?.persistent,
            imageComment = imageComment,
            checked = checked,
            designsNumberForSheet = designsNumberForSheet,
            completionDate = completionDate,
            withdrawalDate = withdrawalDate,
            comment = comment,
            calculationCard = null
        )
    )

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
    override val workflowStageStops: MutableList<WorkflowStageStop> =
        toBizWorkflowStageStop(persistent.workflowStageStops)
    override var bindingForm: BindingForm by delegate(
        persistent.bindingForm,
        ::BindingFormImpl,
        BindingForm::class.java
    )
    override var calculationCard: CalculationCard?
        get() = TODO("Not yet implemented")
        set(value) {}
    override var netSize: Size by delegate(persistent.netSize, ::SizeImpl, Size::class.java)
    override var client: Client by delegate(persistent.client, ::ClientImpl, Client::class.java)
    override fun addWorkflowStageStop(
        comment: String?,
        lastWorkflowStage: Boolean,
        assignTime: LocalDateTime?,
        createTime: LocalDateTime,
        worker: Worker?,
        workflowStage: WorkflowStage
    ): WorkflowStageStop {
        val wss = WorkflowStageStopImpl(
            comment,
            lastWorkflowStage,
            assignTime,
            createTime,
            worker as WorkerImpl?,
            workflowStage as WorkflowStageImpl,
            this
        )
        workflowStageStops.add(wss)
        return wss
    }

    override fun addOrderEnobling(annotation: String?, enobling: Enobling, bindery: Bindery): OrderEnobling {
        val new = OrderEnoblingImpl(
            enobling as EnoblingInt,
            bindery as BinderyImpl,
            this,
            annotation
        )
        orderEnoblings.add(new)
        return new
    }

    override fun addPaperOrderType(
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
    ): PaperOrderType {
        val new = PaperOrderTypeImpl(
            grammage = grammage.toDouble(),
            stockCirculation = stockCirculation,
            sheetNumber = sheetNumber,
            comment = comment,
            circulation = circulation,
            platesQuantityForPrinter = platesQuantityForPrinter,
            paperType = paperType as PaperTypeImpl,
            printer = printer as PrinterImpl,
            colours = colouring as ColouringImpl,
            imposition = impositionType as ImpositionTypeImpl,
            order = this,
            size = size as SizeImpl,
            productionSize = productionSize as SizeImpl,
        )
        this.paperOrderTypes.add(new)
        return new
    }
}

fun Order(
    name: String,
    netSize: Size,
    comment: String?,
    withdrawalDate: LocalDateTime?,
    completionDate: LocalDateTime?,
    designsNumberForSheet: Int,
    checked: Boolean,
    imageComment: String?,
    towerCut: Boolean,
    folding: Boolean,
    client: Client,
    realizationDate: LocalDateTime,
    caretionDate: LocalDateTime,
    pages: Int,
    url: URL?,
    bindery: Bindery,
    salesman: Salesman,
    bindingForm: BindingForm
): Order {
    return OrderImpl(
        name = name,
        netSize = netSize as SizeImpl,
        comment = comment,
        withdrawalDate = withdrawalDate,
        completionDate = completionDate,
        designsNumberForSheet = designsNumberForSheet,
        checked = checked,
        imageComment = imageComment,
        towerCut = towerCut,
        folding = folding,
        client = client as ClientImpl,
        realizationDate = realizationDate,
        caretionDate = caretionDate,
        pages = pages,
        url = url as URLImpl?,
        bindery = bindery as BinderyImpl,
        salesman = salesman as SalesmanImpl,
        bindingForm = bindingForm as BindingFormImpl
    )
}