package pl.macia.printinghouse.web.cli

import io.kvision.form.FormPanel
import io.kvision.form.ValidationStatus
import io.kvision.form.select.TomSelect
import io.kvision.form.select.TomSelectCallbacks
import io.kvision.form.select.TomSelectOptions
import io.kvision.html.label
import io.kvision.panel.SimplePanel
import io.kvision.panel.hPanel
import io.kvision.state.ObservableListWrapper
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import io.kvision.utils.obj
import kotlinx.serialization.Serializable
import pl.macia.printinghouse.response.SizeResp
import pl.macia.printinghouse.web.dao.SizeDao

class SizesTab(responses: List<SizeResp>) : SimplePanel() {
    val responsesRummary = ObservableListWrapper<SizeResp>()
    val form = FormPanel<SizeResp>()
    init {
        val selected: ObservableValue<SizeResp?> = ObservableValue(null)
        responsesRummary.addAll(responses)
        insertUpdateTable( //todo these could be regular tabulator
            summaryList = responsesRummary,
            columnsDef = listOf(
                ColumnDefinition("height", "heigth"),
                ColumnDefinition("width", "width"),
                ColumnDefinition("name", "name"),
            ),
            onSelected = {
                selected.value = it
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

class SizeInput(label: String?) : SimplePanel() {
    private val allSizes = mutableMapOf<String, SizeResp>()
    private val sizeWidth = DoubleInputField("width")
    private val sizeHeight = DoubleInputField("Height")
    private val sizeNameSel = TomSelect(
        tsOptions = TomSelectOptions(
            preload = true
        ), tsCallbacks = TomSelectCallbacks(
            load = { _, c ->
                SizeDao().allNamedSizes(
                    onFulfilled = { fetched ->
                        val v = fetched.filter { it.name != null }.map {
                            val keyVal = it.name
                                ?: throw RuntimeException("null name passed to TomSelect, probably null fetched from database")
                            allSizes[keyVal] =
                                it
                            obj {
                                this.value = keyVal
                                this.text = keyVal
                            }
                        }.toTypedArray()
                        c(v)
                    }, onRejected = {
                        TODO("on rejected not defined")
                    }
                )
            }, shouldLoad = {
                false
            }
        )
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