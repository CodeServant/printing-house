package pl.macia.printinghouse.web.cli

import io.kvision.core.*
import io.kvision.form.FormPanel
import io.kvision.form.check.CheckBox
import io.kvision.form.select.TomSelect
import io.kvision.form.select.select
import io.kvision.form.time.DateTime
import io.kvision.html.label
import io.kvision.panel.HPanel
import io.kvision.panel.SimplePanel
import io.kvision.panel.hPanel
import io.kvision.state.ObservableList
import io.kvision.state.ObservableListWrapper
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
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
    var paperOrderTypes: MutableList<PaperOrderTypeInputData>,
    var orderEnoblings: MutableList<OrderEnoblingInputData>,
    var imageUrl: String?,
    var imageComment: String?,
    var bindery: String,
    var salesman: String,
    var bindingForm: String,
    var calculationCard: CalculationCardInputData?,
    var netSize: SizeInputData,
    var client: String
)

class InsertOrderPanel : SimplePanel() {
    private val orderForm = FormPanel(serializer = OderFormData.serializer())
    private val paperOrderTypes: ObservableList<PaperOrderTypeInput> = ObservableListWrapper()
    private val orderEnoblings: ObservableList<OrderEnoblingInput> = ObservableListWrapper()
    private val netSizeInput = SizeInput("net size")

    init {
        val reqMsg = "value is required"

        orderForm.add(OderFormData::name, TextInput("Name"), required = true, requiredMessage = reqMsg)
        orderForm.add(OderFormData::comment, TextInput("Comment"), required = false)
        orderForm.add(
            key = OderFormData::client,
            control = TomSelect(
                options = listOf(
                    "1" to "Marian Paździoch",
                    "2" to "Evil Corp sp. zł o.o.",
                ), label = "Client"
            )
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
            key = OderFormData::checked,
            control = CheckBox(label = "Checked")
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
            control = TomSelect(
                options = listOf(
                    "1" to "A1",
                    "2" to "A2",
                ), label = "Bindery"
            )
        )
        orderForm.add(
            key = OderFormData::salesman,
            control = TomSelect(
                options = listOf(
                    "1" to "Robert de Niro",
                    "2" to "Danny Devito",
                ), label = "Salesman"
            )
        )
        orderForm.add(
            key = OderFormData::bindingForm,
            control = TomSelect(
                options = listOf(
                    "1" to "Super Bind",
                    "2" to "Bind 2000",
                ), label = "Binding Form"
            )
        )
        orderForm.add(netSizeInput)

        orderForm.add(PaperOrderTypesInput(paperOrderTypes))

        orderForm.add(OrderEnoblingsInput(orderEnoblings))

        orderForm.add(CalculationCardInput())
        add(orderForm)
    }

    /**
     * Fetches from data if validated correctly.
     * @param markFields if form fields must be marked for the user to see
     */
    fun getOrderFormData(markFields: Boolean): OderFormData? {
        val isValidForm = orderForm.validate()
        if (!isValidForm) return null
        TODO("validate all the other input panels")
        val sizeData = netSizeInput.getFormData(markFields) ?: return null
        var orderData = OderFormData(
            name = orderForm[OderFormData::name]!!,
            comment = orderForm[OderFormData::comment],
            designsNumberForSheet = orderForm[OderFormData::designsNumberForSheet]!!,
            checked = orderForm[OderFormData::checked]!!,
            towerCut = orderForm[OderFormData::towerCut]!!,
            folding = orderForm[OderFormData::folding]!!,
            realizationDate = orderForm[OderFormData::realizationDate]!!,
            pages = orderForm[OderFormData::pages]!!,
            paperOrderTypes = TODO(),
            orderEnoblings = TODO(),
            imageUrl = orderForm[OderFormData::imageUrl],
            imageComment = orderForm[OderFormData::imageComment],
            bindery = orderForm[OderFormData::bindery]!!,
            salesman = orderForm[OderFormData::salesman]!!,
            bindingForm = orderForm[OderFormData::bindingForm]!!,
            calculationCard = TODO(),
            netSize = sizeData,
            client = orderForm[OderFormData::client]!!
        )
    }
}

@Serializable
data class PaperOrderTypeInputData(
    var paperType: String,
    var grammage: Double,
    var colours: ColouringSummary,
    var circulation: Int,
    var stockCirculation: Int,
    var sheetNumber: Int,
    var comment: String?,
    var printer: String?,
    var platesQuantityForPrinter: Int,
    var imposition: String,
    var size: SizeSummary,
    var productionSize: SizeSummary,
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
    MultiInput<PaperOrderTypeInput>(paperOrderTypes, ::PaperOrderTypeInput)

private class OrderEnoblingsInput(orderEnoblings: ObservableList<OrderEnoblingInput>) :
    MultiInput<OrderEnoblingInput>(orderEnoblings, ::OrderEnoblingInput)

class PaperOrderTypeInput : SimplePanel() {
    init {
        addBsBorder(BsBorder.BORDER)
        background = Background(color = Color.name(Col.LIGHTCYAN))
        label("paper order type")
        hPanel {
            doubleInputField("grammage")
            add(IntegerInput("stockCirculation"))
            add(IntegerInput("sheetNumber"))
        }
        add(textInput("comment"))
        hPanel {
            add(IntegerInput("circulation"))
            add(IntegerInput("platesQuantityForPrinter"))
            add(SelectPaperType(label = "paper type"))
            add(SelectPrinter(label = "printer"))
        }
        add(ColourInput())
        add(SelectImpositionType())
        add(SizeInput("size"))
        add(SizeInput("production size"))
    }
}

@Serializable
data class OrderEnoblingInputData(
    var orderEnobid: Int?,
    var annotation: String?,
    var enobling: String,
    var bindery: String
)

class OrderEnoblingInput : HPanel() {
    init {
        textInput("annotation")
        select(
            label = "enobling", options = listOf(
                "1" to "farba kolorowa",
                "2" to "karton aksamitny",
                "3" to "papier błysk",
                "4" to "szalony wykrojnik",
                "5" to "wykrojnik zwyczajny",
            )
        )
        select(
            label = "bindery", options = listOf(
                "A1" to "A1",
                "A2" to "A2",
                "A3" to "A3",
                "A4" to "A4",
            )
        )
    }
}

@Serializable
data class CalculationCardInputData(
    var transport: String,
    var otherCosts: String,
    var enoblingCost: String,
    var bindingCost: String,
    var printCosts: MutableList<PrintCostInputData>
)

class CalculationCardInput : SimplePanel() {
    init {
        val printCostsInp = ObservableListWrapper<PrintCostInput>()
        textInput("transport")
        textInput("otherCosts")
        textInput("enoblingCost")
        textInput("bindingCost")

        printCostsInp.add(PrintCostInput())
        val printCostAdd = AddButton {
            onClick {
                printCostsInp.add(PrintCostInput())
            }
        }
        printCostsInp.subscribe {
            it.forEachIndexed { i, element ->
                this.add(element)
                if (it.size - 1 == i)
                    this.add(printCostAdd)
            }
        }
    }
}

@Serializable
data class PrintCostInputData(
    var printCost: String,
    var matrixCost: String,
    var printer: String
)

class PrintCostInput : SimplePanel() {
    init {
        textInput("printCost")
        textInput("matrixCost")
        textInput("printer")
    }
}