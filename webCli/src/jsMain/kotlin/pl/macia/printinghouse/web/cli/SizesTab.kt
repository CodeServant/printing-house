package pl.macia.printinghouse.web.cli

import io.kvision.form.select.TomSelect
import io.kvision.html.label
import io.kvision.panel.SimplePanel
import io.kvision.panel.hPanel
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import kotlinx.serialization.Serializable

@Serializable
data class SizeSummary(
    var height: Double,
    var width: Double,
    var name: String
)

class SizesTab : SimplePanel() {
    init {
        val selected: ObservableValue<SizeSummary?> = ObservableValue(null)
        insertUpdateTable(
            summaryList = listOf(
                SizeSummary(841.0, 594.0, "A1"),
                SizeSummary(594.0, 420.0, "A2"),
            ),
            columnsDef = listOf(
                ColumnDefinition("height", "height"),
                ColumnDefinition("width", "width"),
                ColumnDefinition("name", "name"),
            ),
            onSelected = {
                selected.value = it
            },
            formPanel = {
                SimplePanel {
                    val name = textInput("name")
                    val height = doubleInputField(label = "height")
                    val width = doubleInputField(label = "width")
                    selected.subscribe {
                        name.value = it?.name
                        width.value = it?.width
                        height.value = it?.height
                    }
                }
            }
        )
    }
}

class SizeInput(label: String?) : SimplePanel() {
    private val allSizes = mapOf(
        "A4" to SizeSummary(width = 210.0, height = 297.0, name = "A4"),
        "B5" to SizeSummary(width = 176.0, height = 250.0, name = "B5"),
    )
    val sizeWidth = DoubleInputField("width")
    val sizeHeight = DoubleInputField("Height")
    val tomSel = TomSelect(
        options = listOf(
            "A4" to "A4",
            "B5" to "B5",
        )
    )

    init {
        if (label != null)
            label(label) {}
        hPanel {
            add(sizeWidth)
            add(sizeHeight)
            tomSel.subscribe {
                val notNull = it != null
                if (notNull) {
                    sizeHeight.value = allSizes[it]?.height
                    sizeWidth.value = allSizes[it]?.width
                }
                sizeWidth.disabled = notNull
                sizeHeight.disabled = notNull
            }
            add(tomSel)
        }
    }
}