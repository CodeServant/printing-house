package pl.macia.printinghouse.server.bmodel

import java.math.BigDecimal
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
        towerCut: Boolean,
        folding: Boolean,
        client: ClientImpl,
        realizationDate: LocalDateTime,
        caretionDate: LocalDateTime,
        pages: Int,
        imageUrl: ImageImpl?,
        bindery: BinderyImpl,
        salesman: SalesmanImpl,
        bindingForm: BindingFormImpl
    ) : this(
        POrder(
            name = name,
            netSize = netSize.persistent,
            netSizeWidth = netSize.width,
            netSizeHeight = netSize.heigth,
            pages = pages,
            supervisor = salesman.persistent,
            client = client.persistent,
            creationDate = caretionDate,
            realizationDate = realizationDate,
            bindingForm = bindingForm.persistent,
            bindery = bindery.persistent,
            folding = folding,
            towerCut = towerCut,
            imageURL = imageUrl?.persistent,
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
    override var towerCut: Boolean by persistent::towerCut
    override var folding: Boolean by persistent::folding
    override var realizationDate: LocalDateTime by persistent::realizationDate
    override var creationDate: LocalDateTime by persistent::creationDate
    override var pages: Int by persistent::pages
    override val paperOrderTypes: MutableList<PaperOrderType> = toBizPaperOrderType(persistent.paperOrderTypes)
    override val orderEnoblings: MutableList<OrderEnobling> = toBizOrderEnobling(persistent.orderEnoblings)
    override var imageUrl: Image? by delegate(persistent::imageURL, ::ImageImpl, Image::class.java)
    override var bindery: Bindery by delegate(persistent::bindery, ::BinderyImpl, Bindery::class.java)
    override var salesman: Salesman by delegate(persistent::supervisor, ::SalesmanImpl, Salesman::class.java)
    override val workflowStageStops: MutableList<WorkflowStageStop> =
        toBizWorkflowStageStop(persistent.workflowStageStops)
    override var bindingForm: BindingForm by delegate(
        persistent::bindingForm,
        ::BindingFormImpl,
        BindingForm::class.java
    )
    override var calculationCard: CalculationCard? by delegate(
        persistent::calculationCard,
        ::CalculationCardImpl,
        CalculationCard::class.java
    )
    override var netSize: Size
        get() = if (persistent.netSize != null) SizeImpl(persistent.netSize!!) else SizeImpl(
            persistent.netSizeWidth!!,
            persistent.netSizeHeight!!
        )
        set(value) {
            fun setNew(newSize: SizeImpl) {
                persistent.netSize = newSize.persistent
                persistent.netSizeWidth = newSize.width
                persistent.netSizeHeight = newSize.heigth
            }
            if (value.sizeId == null && value.name == null) {
                setNew(SizeImpl(value.width, value.heigth))
            } else if (value.sizeId == null && value.name != null) {
                setNew(SizeImpl(value.name!!, value.width, value.heigth))
            } else if (value.sizeId != null && value.name != null) {
                setNew(value as SizeImpl)
            }
        }
    override var client: Client by delegate(persistent::client, ::ClientImpl, Client::class.java)
    override fun addWorkflowStageStop(
        comment: String?,
        assignTime: LocalDateTime?,
        createTime: LocalDateTime,
        completionTime: LocalDateTime?,
        worker: Worker?,
        workflowDirEdge: WorkflowDirEdge
    ): WorkflowStageStop {
        val wss = WorkflowStageStopImpl(
            comment,
            assignTime,
            createTime,
            completionTime,
            worker as WorkerImpl?,
            workflowDirEdge as WorkflowDirEdgeImpl,
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

    override fun setCalculationCard(
        transport: BigDecimal,
        otherCosts: BigDecimal,
        enoblingCost: BigDecimal,
        bindingCost: BigDecimal
    ): CalculationCard {
        this.persistent.setCalculationCard(
            bindingCost,
            enoblingCost,
            otherCosts,
            transport
        )
        val calc = CalculationCardImpl(persistent.calculationCard!!)
        calculationCard = calc
        return calculationCard!!
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
    towerCut: Boolean,
    folding: Boolean,
    client: Client,
    realizationDate: LocalDateTime,
    caretionDate: LocalDateTime,
    pages: Int,
    imageUrl: Image?,
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
        towerCut = towerCut,
        folding = folding,
        client = client as ClientImpl,
        realizationDate = realizationDate,
        caretionDate = caretionDate,
        pages = pages,
        imageUrl = imageUrl as ImageImpl?,
        bindery = bindery as BinderyImpl,
        salesman = salesman as SalesmanImpl,
        bindingForm = bindingForm as BindingFormImpl
    )
}