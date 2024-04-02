package pl.macia.printinghouse.web.cli

import io.kvision.form.ValidationStatus
import io.kvision.form.select.TomSelect
import io.kvision.html.label
import io.kvision.panel.SimplePanel
import io.kvision.panel.hPanel
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import kotlinx.serialization.Serializable
import pl.macia.printinghouse.response.SizeResp

@Serializable
data class SizeSummary(
    var height: Double,
    var width: Double,
    var name: String
)

class SizesTab : SimplePanel() {
    init {
        val selected: ObservableValue<SizeSummary?> = ObservableValue(null)
        insertUpdateTable(
            summaryList = listOf(
                SizeSummary(841.0, 594.0, "A1"),
                SizeSummary(594.0, 420.0, "A2"),
            ),
            columnsDef = listOf(
                ColumnDefinition("height", "height"),
                ColumnDefinition("width", "width"),
                ColumnDefinition("name", "name"),
            ),
            onSelected = {
                selected.value = it
            },
            formPanel = {
                SimplePanel {
                    val name = textInput("name")
                    val height = doubleInputField(label = "height")
                    val width = doubleInputField(label = "width")
                    selected.subscribe {
                        name.value = it?.name
                        width.value = it?.width
                        height.value = it?.height
                    }
                }
            }
        )
    }
}

@Serializable
data class SizeInputData(
    val height: Double,
    val width: Double,
    val name: String?
)

class SizeInput(label: String?, sizeResp: List<SizeResp>) : SimplePanel() {
    private val allSizes = mapOf(
        *sizeResp.map {
            it.name to it
        }.toTypedArray()
    )
    private val sizeWidth = DoubleInputField("width")
    private val sizeHeight = DoubleInputField("Height")
    private val sizeNameSel = TomSelect(
        options = sizeResp
            .filter { it.name != null }
            .map {
                it.name!! to it.name!!
            }
    )

    init {
        if (label != null)
            label(label) {}
        hPanel {
            add(sizeWidth)
            add(sizeHeight)
            sizeNameSel.subscribe {
                val notNull = it != null
                if (notNull) {
                    sizeHeight.value = allSizes[it]?.heigth
                    sizeWidth.value = allSizes[it]?.width
                }
                sizeWidth.disabled = notNull
                sizeHeight.disabled = notNull
            }
            add(sizeNameSel)
        }
    }

    /**
     * @param markForm mark form fields for the user (currently not implemented)
     */
    private fun validate(markForm: Boolean): Boolean {
        val w = sizeWidth.value
        val h = sizeHeight.value
        inline fun correct(num: Number?): Boolean {
            return num != null && num.toDouble() > 0
        }

        fun errorMessage(field: DoubleInputField) {
            if (field.value == null)
                field.validatorError = "value must be provided"
            else if (field.value!!.toDouble() <= 0) field.validatorError = "inappropriate value"
            else {
                field.validatorError = null
                field.validationStatus = ValidationStatus.VALID
            }
        }

        if (markForm) {
            errorMessage(sizeWidth)
            errorMessage(sizeHeight)
        }
        return correct(w) && correct(h)
    }

    /**
     * Get all data if it is valid.
     * @param markForm mark form fields for the user
     */
    fun getFormData(markForm: Boolean): SizeInputData? {
        if (!validate(markForm)) return null
        return SizeInputData(
            width = sizeWidth.value!!.toDouble(),
            height = sizeHeight.value!!.toDouble(),
            name = sizeNameSel.value
        )
    }
}