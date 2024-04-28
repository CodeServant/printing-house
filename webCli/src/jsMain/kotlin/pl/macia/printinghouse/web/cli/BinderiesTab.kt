package pl.macia.printinghouse.web.cli

import io.kvision.core.Container
import io.kvision.form.select.TomSelect
import io.kvision.form.select.TomSelectCallbacks
import io.kvision.form.select.TomSelectOptions
import io.kvision.form.text.Text
import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableListWrapper
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import io.kvision.utils.obj
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import pl.macia.printinghouse.request.BinderyChangeReq
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
        val chosenBindery = ObservableValue<BinderySummary?>(null)

        var binderyFormpanel = BinderyFormPanel {
            chosenBindery.subscribe {
                txt.value = it?.name
            }
        }

        insertUpdateTable(
            summaryList = summaryList,
            columnsDef = listOf(
                ColumnDefinition("Name", "name")
            ),
            onSelected = {
                chosenBindery.value = it
            },
            formPanel = {
                binderyFormpanel = BinderyFormPanel {
                    chosenBindery.subscribe {
                        txt.value = it?.name
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
            },
            onUpdate = {
                val formValue = chosenBindery.value
                if (formValue != null) {
                    val newName = binderyFormpanel.txt.value!!
                    dao.changeBindery(
                        formValue.id,
                        BinderyChangeReq(newName),
                        {
                            val selectedIndex = summaryList.indexOf(formValue)
                            summaryList[selectedIndex] = BinderySummary(formValue.id, newName)
                        },
                        {}
                    )
                }
            }, serializer = serializer()
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

class BinderySelect : TomSelect(label = "Bindery") {
    init {
        tsOptions = TomSelectOptions(
            preload = true
        )
        tsCallbacks = TomSelectCallbacks(
            load = { _, callback ->
                BinderyDao().allBinderies(
                    onFulfilled = { fetched ->
                        val v = fetched.map {
                            obj {
                                this.value = it.id
                                this.text = it.name
                            }
                        }.toTypedArray()
                        callback(v)
                    }, onRejected = {
                        TODO("on rejected not defined")
                    }
                )
            }, shouldLoad = {
                false
            }
        )
    }
}