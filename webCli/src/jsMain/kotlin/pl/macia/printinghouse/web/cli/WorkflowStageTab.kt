package pl.macia.printinghouse.web.cli

import io.kvision.form.select.tomSelectInput
import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer


@Serializable
data class WorkflowStageSummary(
    var id: Int,
    var name: String,
    var managers: Int,
)

class WorkflowStageTab : SimplePanel() {
    init {
        val selected: ObservableValue<WorkflowStageSummary?> = ObservableValue(null)
        insertUpdateTable(
            summaryList = listOf(
                WorkflowStageSummary(1, "Cutting Room", 2),
                WorkflowStageSummary(2, "Bindery", 1),
            ),
            columnsDef = listOf(
                ColumnDefinition("name", "name"),
                ColumnDefinition("managers quantity", "managers"),
            ),
            onSelected = {
                selected.value = it
            },
            formPanel = {
                SimplePanel {
                    val name = textInput("name")
                    tomSelectInput(
                        options = listOf(
                            "one" to "Luciani Albino",
                            "two" to "Ratzinger Joseph",
                            "three" to "Wojtyła Karol",
                        ),
                        multiple = true,
                    )
                }
            }, serializer = serializer()
        )
    }
}
