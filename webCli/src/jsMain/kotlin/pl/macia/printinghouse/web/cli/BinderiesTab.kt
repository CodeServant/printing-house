package pl.macia.printinghouse.web.cli

import io.kvision.core.*
import io.kvision.form.text.Text
import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import kotlinx.serialization.Serializable

@Serializable
data class BinderySummary(val name: String)

class BinderiesTab() : SimplePanel() {
    init {
        val summaryList = listOf(
            BinderySummary("A1"),
            BinderySummary("A2"),
            BinderySummary("B1"),
            BinderySummary("C2"),
        )
        var textFormContent = ObservableValue<String?>(null)

        insertUpdateTable(
            summaryList = summaryList,
            columnsDef = listOf(
                ColumnDefinition("Name", "name")
            ),
            onSelected = {
                textFormContent.value = it?.name
            },
            formPanel = {
                BinderyFormPanel {
                    textFormContent.subscribe {
                        txt.value = it
                    }
                }
            }
        )
    }
}

class BinderyFormPanel(init: (BinderyFormPanel.() -> Unit)? = null) : SimplePanel() {
    var value: String? = null
    var txt: Text

    init {
        txt = textInput("nazwa")
        init?.invoke(this)
    }
}

fun Container.binderyFormPanel(init: (BinderyFormPanel.() -> Unit)? = null): BinderyFormPanel {
    val bind = BinderyFormPanel(init)
    add(bind)
    return bind
}