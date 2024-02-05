package pl.macia.printinghouse.web.cli

import io.kvision.form.select.Select
import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import kotlinx.serialization.Serializable

@Serializable
data class PrinterSummary(
    var name: String,
    var digest: String
)

class PrintersTab : SimplePanel() {
    init {
        val summaryList = listOf(
            PrinterSummary("Mała Komori", "MK"),
            PrinterSummary("Duża Komori", "DK"),
            PrinterSummary("Średnia Komori", "SK"),
        )
        val printerTextFormData = ObservableValue<PrinterSummary?>(null)
        insertUpdateTable(
            summaryList = summaryList,
            columnsDef = listOf(
                ColumnDefinition("Digest", "digest"),
                ColumnDefinition("Name", "name"),
            ),
            onSelected = {
                printerTextFormData.value = it
            },
            formPanel = {
                SimplePanel {
                    val digest = textInput("Digest")
                    val name = textInput("Name")
                    printerTextFormData.subscribe {
                        digest.value = it?.digest
                        name.value = it?.name
                    }
                }
            }
        )
    }
}

class SelectPrinter(label: String? = null) : Select(label = label) {
    init {
        // todo initialize this give a
    }
}