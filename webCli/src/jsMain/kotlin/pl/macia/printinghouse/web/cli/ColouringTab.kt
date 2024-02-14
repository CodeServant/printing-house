package pl.macia.printinghouse.web.cli

import io.kvision.form.number.Numeric
import io.kvision.form.number.numeric
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

class ColouringTab : SimplePanel() {
    init {
        val selected = ObservableValue<ColouringSummary?>(null)
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
                ColourInput {
                    selected.subscribe {
                        firstSide.value = it?.firstSide
                        secondSide.value = it?.secondSide
                    }
                }
            }
        )
    }
}

class ColourInput(init: (ColourInput.() -> Unit)? = null) : HPanel() {
    var firstSide: Numeric
    var secondSide: Numeric

    init {
        firstSide = numeric(min = 0, max = 4, decimals = 0, label = "first side")
        secondSide = numeric(min = 0, max = 4, decimals = 0, label = "second side")
        init?.invoke(this)
    }
}