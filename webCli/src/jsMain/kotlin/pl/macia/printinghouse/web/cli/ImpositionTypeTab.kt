package pl.macia.printinghouse.web.cli

import io.kvision.core.onChange
import io.kvision.form.select.Select
import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import kotlinx.serialization.Serializable

@Serializable
data class ImpositionTypeSummary(
    var id: Int,
    var name: String
)

class ImpositionTypeTab : SimplePanel() {
    val selected: ObservableValue<ImpositionTypeSummary?> = ObservableValue(null)

    init {
        insertUpdateTable(
            summaryList = listOf(
                ImpositionTypeSummary(1, "f/f"),
                ImpositionTypeSummary(1, "z/r"),
            ),
            columnsDef = listOf(
                ColumnDefinition("Name", ImpositionTypeSummary::name.name)
            ),
            onSelected = {
                selected.value = it
            },
            formPanel = {
                SimplePanel {
                    val name = textInput("Name")
                    selected.subscribe {
                        name.value = it?.name
                    }
                }
            }
        )
    }
}

class SelectImpositionType(label: String? = null) : SimplePanel() {
    val select = Select(
        options = listOf(
            Pair("1", "f/f"), //todo change to real data
            Pair("2", "f/o"),
            Pair("3", "f/g"),
        ),
        label = label,
        floating = true
    ) {
        onChange {
            validateSelect(markFields = true)
        }
    }

    init {
        add(select)
    }

    fun getFormData(markFields: Boolean): String? {
        return if (validateSelect(markFields)) select.value else null
    }

    private fun validateSelect(markFields: Boolean): Boolean {
        val valid = select.value != null
        select.validatorError = if (markFields && !valid) "you must select one item" else null
        return valid
    }
}