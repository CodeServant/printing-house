package pl.macia.printinghouse.web.cli

import io.kvision.form.select.TomSelect
import io.kvision.form.select.TomSelectCallbacks
import io.kvision.form.select.TomSelectOptions
import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import io.kvision.utils.obj
import kotlinx.serialization.Serializable
import pl.macia.printinghouse.web.dao.PrinterDao

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

class SelectPrinter(label: String? = null) : TomSelect(label = label) {
    init {
        tsCallbacks = TomSelectCallbacks(
            load = { _, callback ->
                PrinterDao().allPrinters(
                    onFulfilled = { fetched ->
                        val arr = fetched.map { printer ->
                            obj {
                                this.text = printer.name
                                this.value = printer.id
                            }
                        }.toTypedArray()
                        callback(arr)
                    },
                    onRejected = {
                        TODO("implement on rejected fetch in printers")
                    },
                )
            },
            shouldLoad = { false }
        )
        tsOptions = TomSelectOptions(
            preload = true
        )
    }
}