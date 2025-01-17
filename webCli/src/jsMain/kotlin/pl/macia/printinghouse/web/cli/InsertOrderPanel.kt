package pl.macia.printinghouse.web.cli

import io.kvision.core.*
import io.kvision.form.FormPanel
import io.kvision.form.check.CheckBox
import io.kvision.form.select.TomSelect
import io.kvision.form.select.TomSelectCallbacks
import io.kvision.form.select.TomSelectOptions
import io.kvision.form.time.DateTime
import io.kvision.html.button
import io.kvision.html.label
import io.kvision.panel.HPanel
import io.kvision.panel.SimplePanel
import io.kvision.panel.hPanel
import io.kvision.state.*
import io.kvision.utils.obj
import kotlinx.browser.localStorage
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import pl.macia.printinghouse.request.*
import pl.macia.printinghouse.web.StorageInfo
import pl.macia.printinghouse.web.dao.BindingFormDao
import pl.macia.printinghouse.web.dao.SalesmanDao
import kotlin.js.Date

@Serializable
data class OderFormData(
    var name: String,
    var comment: String?,
    var designsNumberForSheet: Int,
    var checked: Boolean,
    var towerCut: Boolean,
    var folding: Boolean,

    /**
     * Expected realization date.
     */
    @Contextual
    var realizationDate: Date,
    var pages: Int,
    var paperOrderTypes: List<PaperOrderTypeInputData>,
    var orderEnoblings: List<OrderEnoblingInputData>,
    var imageUrl: String?,
    var imageComment: String?,
    var bindery: String,
    var salesman: String,
    var bindingForm: String,
    var calculationCard: CalculationCardInputData?,
    var netSize: SizeInputData,
    var client: String,
    var workflowGraph: String
)

class InsertOrderPanel : SimplePanel() {
    private val orderForm = FormPanel(serializer = OderFormData.serializer())
    private val paperOrderTypes: ObservableList<PaperOrderTypeInput> = ObservableListWrapper()
    private val orderEnoblings: ObservableList<OrderEnoblingInput> = ObservableListWrapper()
    private val netSizeInput = SizeInput("net size")
    private val calculationCard = CalculationCardInput()
    private val clientPicker = PickClientForm()

    init {
        val reqMsg = "value is required"
        calculationCard.visible = false
        orderForm.add(OderFormData::name, TextInput("Name"), required = true, requiredMessage = reqMsg)
        orderForm.add(OderFormData::comment, TextInput("Comment"), required = false)
        orderForm.add(clientPicker)
        orderForm.add(
            key = OderFormData::workflowGraph,
            control = WorkflowGraphSelect(),
            required = true
        )
        orderForm.add(
            key = OderFormData::designsNumberForSheet,
            control = IntegerInput("Designs Number for Sheet"),
            required = true,
            requiredMessage = reqMsg
        )
        orderForm.add(
            key = OderFormData::towerCut,
            control = CheckBox(label = "Tower Cut")
        )
        orderForm.add(
            key = OderFormData::folding,
            control = CheckBox(label = "Folding")
        )
        orderForm.add(
            key = OderFormData::realizationDate,
            control = DateTime(label = "Realization Date"),
            required = true
        )
        orderForm.add(
            key = OderFormData::pages,
            control = IntegerInput("Pages"),
            required = true,
            requiredMessage = reqMsg,
        )
        orderForm.add(
            key = OderFormData::imageUrl,
            control = TextInput("Url of draw.io Image"),
            required = false
        )
        orderForm.add(
            key = OderFormData::imageComment,
            control = TextInput("Comment to image"),
            required = false
        )
        orderForm.add(
            key = OderFormData::bindery,
            control = BinderySelect(),
            required = true
        )
        val salesmanSelect = TomSelect(
            label = "Salesman"
        ) {
            this.disabled = true
            val thisEmail = StorageInfo(localStorage).username
                ?: throw RuntimeException("there is no email in local storage")
            SalesmanDao().findByEmail(
                thisEmail,
                onFulfilled = { salesmanResp ->
                    this.options = listOf(
                        Pair(
                            salesmanResp.id.toString(),
                            "${salesmanResp.name} ${salesmanResp.surname}: ${salesmanResp.email}"
                        )
                    )
                    this.value = salesmanResp.id.toString()
                },
                onRejected = {
                    TODO("implement on rejected in InsertOrderPanel salesman")
                }
            )
        }

        orderForm.add(
            key = OderFormData::salesman,
            control = salesmanSelect,
            required = true
        )
        orderForm.add(
            key = OderFormData::bindingForm,
            control = TomSelect(
                label = "Binding Form",
                tsCallbacks = TomSelectCallbacks(
                    load = { _, callback ->
                        BindingFormDao().allBindingForms(
                            onFulfilled = { fetched ->
                                val arr = fetched.map {
                                    obj {
                                        this.value = it.id
                                        this.text = it.name
                                    }
                                }.toTypedArray()
                                callback(arr)
                            }, onRejected = {
                                TODO("implement Binding Form doa on rejected")
                            }
                        )
                    },
                    shouldLoad = { false }
                ),
                tsOptions = TomSelectOptions(
                    preload = true
                )
            ),
            required = true
        )
        orderForm.add(netSizeInput)

        orderForm.add(PaperOrderTypesInput(paperOrderTypes))

        orderForm.add(OrderEnoblingsInput(orderEnoblings))

        orderForm.add(
            button("add calculation card") {
                onClick {
                    calculationCard.visible = !calculationCard.visible
                    if (calculationCard.visible)
                        this.text = "reject calculation card"
                    else
                        this.text = "add calculation card"
                }
            }
        )
        orderForm.add(calculationCard)
        add(orderForm)
    }

    /**
     * Fetches from data if validated correctly.
     * @param markFields if form fields must be marked for the user to see
     */
    fun getFormData(markFields: Boolean): OderFormData? {
        var isValidForm = orderForm.validate(markFields)

        val paperOrderTypes: MutableList<PaperOrderTypeInputData> = mutableListOf()
        this.paperOrderTypes.forEach {
            val papOrdTypData = it.getFormData(markFields)
            val isValidField = papOrdTypData != null
            isValidForm = isValidForm && isValidField
            if (papOrdTypData != null)
                paperOrderTypes.add(papOrdTypData)
        }

        val orderEnoblings: MutableList<OrderEnoblingInputData> = mutableListOf()
        this.orderEnoblings.forEach {
            val ordEnobData = it.getFormData(markFields)
            val isValidField = ordEnobData != null

            isValidForm = isValidForm && isValidField
            if (ordEnobData != null)
                orderEnoblings.add(ordEnobData)
        }
        val netSizeValidField = netSizeInput.getFormData(markFields)
        val validNetSize = netSizeValidField != null

        isValidForm = isValidForm && validNetSize
        val calcData = calculationCard.getFormData(markFields)
        isValidForm = isValidForm && (calcData != null == calculationCard.visible)
        if (!isValidForm) return null
        return OderFormData(
            name = orderForm[OderFormData::name]!!,
            comment = orderForm[OderFormData::comment],
            designsNumberForSheet = orderForm[OderFormData::designsNumberForSheet]!!,
            checked = false, // for salesman it is always false
            towerCut = orderForm[OderFormData::towerCut]!!,
            folding = orderForm[OderFormData::folding]!!,
            realizationDate = orderForm[OderFormData::realizationDate]!!,
            pages = orderForm[OderFormData::pages]!!,
            paperOrderTypes = paperOrderTypes, //todo check quantities requirements for multiple fields
            orderEnoblings = orderEnoblings,
            imageUrl = orderForm[OderFormData::imageUrl],
            imageComment = orderForm[OderFormData::imageComment],
            bindery = orderForm[OderFormData::bindery]!!,
            salesman = orderForm[OderFormData::salesman]!!,
            bindingForm = orderForm[OderFormData::bindingForm]!!,
            calculationCard = calcData,
            netSize = netSizeValidField!!,
            client = clientPicker.getFormData() ?: throw RuntimeException("no client provided"),
            workflowGraph = orderForm[OderFormData::workflowGraph]!!,
        )
    }
}

@Serializable
data class PaperOrderTypeInputData(
    var paperType: String,
    var grammage: Double,
    var colours: ColouringInputData,
    var circulation: Int,
    var stockCirculation: Int,
    var sheetNumber: Int,
    var comment: String?,
    var printer: String,
    var platesQuantityForPrinter: Int,
    var imposition: String,
    var size: SizeInputData,
    var productionSize: SizeInputData,
)

/**
 * Panel consisting of multiple and varying quantity of input fields.
 */
private open class MultiInput<Input : Container>(inputFields: ObservableList<Input>, newInputField: () -> Input) :
    SimplePanel() {
    init {
        inputFields.add(newInputField())
        val papOrdTypButton = AddButton {
            onClick {
                inputFields.add(newInputField())
            }
        }
        inputFields.subscribe {
            it.forEachIndexed { i, element ->
                add(element)
                if (it.size - 1 == i)
                    add(papOrdTypButton)
            }
        }
    }
}

private class PaperOrderTypesInput(paperOrderTypes: ObservableList<PaperOrderTypeInput>) :
    MultiInput<PaperOrderTypeInput>(
        paperOrderTypes,
        {
            PaperOrderTypeInput()
        }
    )

private class OrderEnoblingsInput(orderEnoblings: ObservableList<OrderEnoblingInput>) :
    MultiInput<OrderEnoblingInput>(orderEnoblings, ::OrderEnoblingInput)

class PaperOrderTypeInput : SimplePanel() {
    private var grammageF = DoubleInputField("grammage")
    private var stockCirculationF = IntegerInput("stockCirculation")
    private var sheetNumberF = IntegerInput("sheetNumber")
    private val commentF = TextInput("comment")
    private val circulationF = IntegerInput("circulation")
    private val platesQuantityForPrinterF = IntegerInput("platesQuantityForPrinter")
    private val paperTypeF = SelectPaperType(label = "paper type")
    private val printerF = SelectPrinter(label = "printer")
    private val colourF = ColourInput()
    private val impositionTypeF = SelectImpositionType("imposition type")
    private val sizeF = SizeInput("size")
    private val productionSizeF = SizeInput("production size")
    val form = FormPanel<PaperOrderTypeInputData>()

    init {
        addBsBorder(BsBorder.BORDER, BsBorder.BORDERINFO)
        background = Background(color = Color.name(Col.LIGHTCYAN))
        label("paper order type")
        hPanel {
            form.add(PaperOrderTypeInputData::grammage, grammageF, required = true)
            form.add(PaperOrderTypeInputData::stockCirculation, stockCirculationF, required = true)
            form.add(PaperOrderTypeInputData::sheetNumber, sheetNumberF, required = true)
        }
        form.add(PaperOrderTypeInputData::comment, commentF)
        hPanel {
            form.add(PaperOrderTypeInputData::circulation, circulationF, required = true)
            form.add(PaperOrderTypeInputData::platesQuantityForPrinter, platesQuantityForPrinterF, required = true)
            form.add(PaperOrderTypeInputData::paperType, paperTypeF, required = true)
            form.add(PaperOrderTypeInputData::printer, printerF, required = true)
        }
        //todo validate all below fields
        form.add(colourF)
        form.add(impositionTypeF)
        form.add(sizeF)
        form.add(productionSizeF)
        add(form)
    }

    fun getFormData(markFields: Boolean): PaperOrderTypeInputData? {
        var valid = form.validate(markFields)

        val colorFetched = colourF.getFormData(markFields)
        valid = valid && colorFetched != null

        val impositionTypeFetched = impositionTypeF.getFormData(markFields)
        valid = valid && impositionTypeFetched != null

        val sizeFetched = sizeF.getFormData(markFields)
        valid = valid && sizeFetched != null

        val productionSizeFetched = productionSizeF.getFormData(markFields)
        valid = valid && productionSizeFetched != null


        if (!valid) return null
        return PaperOrderTypeInputData(
            paperType = paperTypeF.value ?: return null,
            grammage = grammageF.value?.toDouble() ?: return null,
            colours = colorFetched ?: return null,
            circulation = circulationF.value?.toInt() ?: return null,
            stockCirculation = stockCirculationF.value?.toInt() ?: return null,
            sheetNumber = sheetNumberF.value?.toInt() ?: return null,
            comment = commentF.value,
            printer = printerF.value ?: return null,
            platesQuantityForPrinter = platesQuantityForPrinterF.value?.toInt() ?: return null,
            imposition = impositionTypeFetched ?: return null,
            size = sizeFetched ?: return null,
            productionSize = productionSizeFetched ?: return null,
        )
    }
}

@Serializable
data class OrderEnoblingInputData(
    var annotation: String?,
    var enobling: String,
    var bindery: String
)

class OrderEnoblingInput : HPanel() {
    val form = FormPanel<OrderEnoblingInputData>()

    init {
        val annotation = TextInput("annotation")
        val enobling = EnoblingSelect()

        val bindery = BinderySelect()
        form.add(key = OrderEnoblingInputData::annotation, control = annotation, required = false)
        form.add(key = OrderEnoblingInputData::enobling, control = enobling, required = true)
        form.add(key = OrderEnoblingInputData::bindery, control = bindery, required = true)
        add(form)
    }

    fun getFormData(markFields: Boolean): OrderEnoblingInputData? {
        val valid = form.validate(markFields)
        return if (valid) {
            OrderEnoblingInputData(
                annotation = form[OrderEnoblingInputData::annotation],
                enobling = form[OrderEnoblingInputData::enobling]!!,
                bindery = form[OrderEnoblingInputData::bindery]!!
            )
        } else null
    }
}

@Serializable
data class CalculationCardInputData(
    var transport: String,
    var otherCosts: String,
    var enoblingCost: String,
    var bindingCost: String,
    var printCosts: List<PrintCostInputData>
)

class CalculationCardInput : SimplePanel() {
    val form = FormPanel<CalculationCardInputData>()
    private val printCostsInp = ObservableListWrapper<PrintCostInput>()

    init {

        form.add(
            CalculationCardInputData::transport,
            textInput("transport"),
            required = true
        ) //todo validation of the money field
        form.add(CalculationCardInputData::otherCosts, textInput("otherCosts"), required = true)
        form.add(CalculationCardInputData::enoblingCost, textInput("enoblingCost"), required = true)
        form.add(CalculationCardInputData::bindingCost, textInput("bindingCost"), required = true)
        add(form)
        add(PrintCostsInput(printCostsInp))
    }

    fun getFormData(markFields: Boolean): CalculationCardInputData? {
        var valid = form.validate(markFields)
        val printCosts: MutableList<PrintCostInputData> = mutableListOf()
        printCostsInp.forEach {
            val printCost = it.getFormdata(markFields)
            val printValid = printCost != null
            valid = valid && printValid
            if (printValid)
                printCosts.add(printCost)
        }

        if (!valid) return null
        return CalculationCardInputData(
            transport = form[CalculationCardInputData::transport]!!,
            otherCosts = form[CalculationCardInputData::otherCosts]!!,
            enoblingCost = form[CalculationCardInputData::enoblingCost]!!,
            bindingCost = form[CalculationCardInputData::bindingCost]!!,
            printCosts = printCosts
        )
    }
}

private class PrintCostsInput(printCosts: ObservableList<PrintCostInput>) :
    MultiInput<PrintCostInput>(printCosts, ::PrintCostInput)

@Serializable
data class PrintCostInputData(
    var printCost: String,
    var matrixCost: String,
    var printer: String
)

class PrintCostInput : SimplePanel() {
    val form = FormPanel<PrintCostInputData>()

    init {
        form.add(
            PrintCostInputData::printCost,
            textInput("printCost"),
            required = true
        ) //todo validation of the money field
        form.add(
            PrintCostInputData::matrixCost,
            textInput("matrixCost"),
            required = true
        )
        form.add(PrintCostInputData::printer, SelectPrinter("printer"), required = true)
        add(form)
    }

    fun getFormdata(markFields: Boolean): PrintCostInputData? {
        if (form.validate(markFields)) {
            return PrintCostInputData(
                printCost = form[PrintCostInputData::printCost].toString(),
                matrixCost = form[PrintCostInputData::matrixCost].toString(),
                printer = form[PrintCostInputData::printer].toString()
            )
        }
        return null
    }
}

// todo test this function if works correctly
fun OderFormData.toTransport(): OrderReq {
    val imageUrl = if (imageUrl != null && imageUrl!!.isNotEmpty())
        ImageReq(imageUrl!!, imageComment) else null
    return OrderReq(
        name = name,
        comment = comment,
        designsNumberForSheet = designsNumberForSheet,
        checked = checked,
        towerCut = towerCut,
        folding = folding,
        realizationDate = LocalDateTime(
            realizationDate.getFullYear(),
            realizationDate.getMonth(),
            realizationDate.getDay(),
            realizationDate.getHours(),
            realizationDate.getMinutes()
        ),
        pages = pages,
        paperOrderTypes = paperOrderTypes.map {
            it.toTransport()
        },
        orderEnoblings = orderEnoblings.map {
            it.toTransport()
        },
        imageUrl = imageUrl,
        binderyId = bindery.toInt(),
        salesmanId = salesman.toInt(),
        bindingFormId = bindingForm.toInt(),
        calculationCard = calculationCard?.toTransport(),
        netSize = netSize.toTransport(),
        clientId = client.toInt(),
        workflowDirGraphId = workflowGraph.toInt(),
        completionDate = null,
        creationDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        withdrawalDate = null,
    )
}

fun PaperOrderTypeInputData.toTransport(): PaperOrderTypeReq {
    return PaperOrderTypeReq(
        grammage = grammage,
        stockCirculation = stockCirculation,
        sheetNumber = sheetNumber,
        comment = comment,
        circulation = circulation,
        platesQuantityForPrinter = platesQuantityForPrinter,
        paperTypeId = paperType.toInt(),
        printerId = printer.toInt(),
        colouring = ColouringPartReq(colours.firstSide, colours.secondSide),
        impositionTypeId = imposition.toInt(),
        size = size.toTransport(),
        productionSize = productionSize.toTransport(),
    )
}

fun OrderEnoblingInputData.toTransport(): OrderEnoblingReq {
    return OrderEnoblingReq(
        annotation = annotation,
        enoblingId = enobling.toInt(),
        binderyId = bindery.toInt(),
    )
}

fun SizeInputData.toTransport(): SizeReq {
    return SizeReq(
        width = width,
        heigth = height
    )
}

fun CalculationCardInputData.toTransport(): CalculationCardReq {
    return CalculationCardReq(
        transport = transport,
        otherCosts = otherCosts,
        bindingCost = bindingCost,
        enoblingCost = enoblingCost,
        printCosts = printCosts.map {
            it.toTransport()
        }
    )
}

fun PrintCostInputData.toTransport(): PrintCostReq {
    return PrintCostReq(
        printCost = printCost,
        matrixCost = matrixCost,
        printerId = printer.toInt()
    )
}