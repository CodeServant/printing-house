package pl.macia.printinghouse.web.cli

import io.kvision.core.onChange
import io.kvision.form.select.TomSelect
import io.kvision.form.select.TomSelectCallbacks
import io.kvision.form.select.TomSelectOptions
import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import io.kvision.utils.obj
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import pl.macia.printinghouse.web.dao.ImpositionTypeDao

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
            }, serializer = serializer()
        )
    }
}

class SelectImpositionType(label: String? = null) : SimplePanel() {
    val select = TomSelect(
        tsCallbacks = TomSelectCallbacks(
            load = { _, callback ->
                ImpositionTypeDao().allImpositionTypes(
                    onFulfilled = { imTpResps ->
                        val arr = imTpResps.map {
                            obj {
                                this.text = it.name
                                this.value = it.id
                            }
                        }.toTypedArray()
                        callback(arr)
                    },
                    onRejected = {
                        TODO("allImpositionTypes on rejected not implemented")
                    }
                )
            },
            shouldLoad = { false }
        ), tsOptions = TomSelectOptions(
            preload = true
        ),
        label = label
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