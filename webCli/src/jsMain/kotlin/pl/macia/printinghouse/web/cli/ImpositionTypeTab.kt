package pl.macia.printinghouse.web.cli

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
    init {
        // todo implement
    }
}