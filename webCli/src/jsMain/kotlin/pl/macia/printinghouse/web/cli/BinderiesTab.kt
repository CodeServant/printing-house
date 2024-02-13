package pl.macia.printinghouse.web.cli

import io.kvision.core.*
import io.kvision.form.text.Text
import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import kotlinx.serialization.Serializable
import pl.macia.printinghouse.response.BinderyResp

@Serializable
data class BinderySummary(val id: Int, val name: String)

class BinderiesTab(binderies: List<BinderyResp>) : SimplePanel() {
    init {
        val summaryList = mutableListOf<BinderySummary>()
        binderies.forEach {
            summaryList.add(BinderySummary(it.id, it.name))
        }
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