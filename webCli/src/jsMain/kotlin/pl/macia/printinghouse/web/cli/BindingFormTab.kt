package pl.macia.printinghouse.web.cli

import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import kotlinx.serialization.Serializable


@Serializable
data class BindingFormSummary(
    var name: String
)

class BindingFormTab : SimplePanel() {
    init {
        val textFormContent = ObservableValue<BindingFormSummary?>(null)

        insertUpdateTable(
            summaryList = listOf(
                BindingFormSummary("karta"),
                BindingFormSummary("kamień błysk"),
                BindingFormSummary("karton gips"),
                BindingFormSummary("gips gładki"),
            ),
            columnsDef = listOf(
                ColumnDefinition("Name", "name")
            ),
            onSelected = {
                textFormContent.value = it //todo every time i create the same value maby move it somewhere else
            },
            formPanel = {
                SimplePanel {
                    val text = textInput("Name")
                    textFormContent.subscribe {
                        text.value = it?.name
                    }
                }
            }
        )
    }
}