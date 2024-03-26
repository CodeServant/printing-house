package pl.macia.printinghouse.web.cli

import io.kvision.form.FormPanel
import io.kvision.form.number.Numeric
import io.kvision.panel.HPanel
import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import kotlinx.serialization.Serializable

@Serializable
data class ColouringSummary(
    var id: Byte,
    var firstSide: Byte,
    var secondSide: Byte
)

@Serializable
data class ColouringInputData(
    val firstSide: Byte,
    val secondSide: Byte
)

class ColouringTab : SimplePanel() {
    init {
        val selected = ObservableValue<ColouringSummary?>(null)
        var colorInput: ColourInput? = null
        insertUpdateTable(
            summaryList = listOf(
                ColouringSummary(1, 1, 1),
                ColouringSummary(2, 2, 0),
                ColouringSummary(3, 4, 4),
            ),
            columnsDef = listOf(
                ColumnDefinition("first site", ColouringSummary::firstSide.name),
                ColumnDefinition("second site", ColouringSummary::secondSide.name),
            ),
            onSelected = {
                selected.value = it
            },
            formPanel = {
                colorInput = ColourInput {
                    selected.subscribe {
                        if (it == null)
                            this.form.clearData()
                        else {
                            this.form.setData(ColouringInputData(it.firstSide, it.secondSide))
                        }
                    }
                }
                colorInput
            }
        )
    }
}

class ColourInput(init: (ColourInput.() -> Unit)? = null) : HPanel() {
    val form = FormPanel(serializer = ColouringInputData.serializer())

    init {
        fun commonValidator(side: Numeric): Boolean {
            val side = side.value?.toByte() ?: return false
            return side <= 4 && side >= 0
        }

        fun commonValidationMessage(side: Numeric): String? {
            return if (commonValidator(side)) null
            else
                "colouring suppose to be between 0 and 4"
        }

        fun firstSideLower(secondSide: Numeric): Boolean {
            return form[ColouringInputData::firstSide]?.toByte() ?: 0 < secondSide.value?.toByte() ?: 0
        }
        form.add(
            ColouringInputData::firstSide,
            Numeric(min = 0, max = 4, decimals = 0, label = "first side"),
            validator = ::commonValidator,
            validatorMessage = ::commonValidationMessage,
            required = true
        )
        form.add(
            ColouringInputData::secondSide,
            Numeric(min = 0, max = 4, decimals = 0, label = "second side"),
            validator = {
                println("${commonValidator(it)} validator 2")
                println("${!firstSideLower(it)} !firstSideLower(it)")
                commonValidator(it) && !firstSideLower(it)
            },
            validatorMessage = {
                println("validator msg 2")
                var message = commonValidationMessage(it)
                if (message == null && firstSideLower(it)) {
                    "first side should be equal or greater than second side"
                } else {
                    message
                }
            },
            required = true

        )
        add(form)
        init?.invoke(this)
    }

    /**
     * get the data from an input and validates it
     * @param markField fields of the form will be visibly marked
     */
    fun getFormData(markField: Boolean): ColouringInputData? {
        if (!form.validate(markField)) return null
        return form.getData()
    }
}