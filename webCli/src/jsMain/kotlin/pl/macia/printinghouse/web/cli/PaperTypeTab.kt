package pl.macia.printinghouse.web.cli

import io.kvision.form.select.TomSelect
import io.kvision.form.select.TomSelectCallbacks
import io.kvision.form.select.TomSelectOptions
import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import io.kvision.utils.obj
import kotlinx.serialization.Serializable
import pl.macia.printinghouse.web.dao.PaperTypeDao

@Serializable
data class PaperTypeSummary(
    var name: String,
    var shortName: String,
)

class PaperTypeTab : SimplePanel() {
    init {
        val textFormContent = ObservableValue<PaperTypeSummary?>(null)

        insertUpdateTable(
            summaryList = listOf(
                PaperTypeSummary("Papier Błysk", "Pap Błysk"),
                PaperTypeSummary("Papier Offsetowy", "Offsetowy"),
                PaperTypeSummary("papier Kredowy", "kreda"),
            ),
            columnsDef = listOf(
                ColumnDefinition("Name", "name"),
                ColumnDefinition("Short Name", "shortName"),
            ),
            onSelected = {
                textFormContent.value = it //todo every time i create the same value maby move it somewhere else
            },
            formPanel = {
                SimplePanel {
                    val name = textInput("Name")
                    val shortName = textInput("Short Name")

                    textFormContent.subscribe {
                        name.value = it?.name
                        shortName.value = it?.shortName
                    }
                }
            }
        )
    }
}

/**
 * Selection panel for paper type.
 */
class SelectPaperType(label: String? = null) : TomSelect(label = label) {
    init {
        tsCallbacks = TomSelectCallbacks(
            load = { _, callback ->
                PaperTypeDao().allPaperTypes(
                    onFulfilled = {
                        callback(
                            it.map {
                                obj {
                                    this.text = it.name
                                    this.value = it.id
                                }
                            }.toTypedArray()
                        )
                    },
                    onRejected = {
                        TODO("implement paper type fetch all rejected")
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