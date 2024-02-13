package pl.macia.printinghouse.web.cli

import io.kvision.core.*
import io.kvision.form.text.Text
import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableListWrapper
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import kotlinx.serialization.Serializable
import pl.macia.printinghouse.request.BinderyReq
import pl.macia.printinghouse.response.BinderyResp
import pl.macia.printinghouse.web.dao.BinderyDao

@Serializable
data class BinderySummary(val id: Int, val name: String)

class BinderiesTab(binderies: List<BinderyResp>, dao: BinderyDao) : SimplePanel() {
    init {
        val summaryList = ObservableListWrapper<BinderySummary>()
        binderies.forEach {
            summaryList.add(BinderySummary(it.id, it.name))
        }
        val textFormContent = ObservableValue<String?>(null)
        var binderyFormpanel = BinderyFormPanel {
            textFormContent.subscribe {
                txt.value = it
            }
        }

        insertUpdateTable(
            summaryList = summaryList,
            columnsDef = listOf(
                ColumnDefinition("Name", "name")
            ),
            onSelected = {
                textFormContent.value = it?.name
            },
            formPanel = {
                binderyFormpanel = BinderyFormPanel {
                    textFormContent.subscribe {
                        txt.value = it
                    }
                }
                binderyFormpanel
            },
            onInsert = {
                val formValue = binderyFormpanel.txt.value
                if (formValue != null) {
                    dao.newBinderyReq(
                        BinderyReq(formValue),
                        {
                            summaryList.add(BinderySummary(it.asInt(), formValue))
                        },
                        {}
                    )
                }

            }
        )
    }
}

class BinderyFormPanel(init: (BinderyFormPanel.() -> Unit)? = null) : SimplePanel() {
    var value: String? = null
    var txt: Text = textInput("nazwa")

    init {
        init?.invoke(this)
    }
}

fun Container.binderyFormPanel(init: (BinderyFormPanel.() -> Unit)? = null): BinderyFormPanel {
    val bind = BinderyFormPanel(init)
    add(bind)
    return bind
}