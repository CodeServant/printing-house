package pl.macia.printinghouse.web.cli

import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableListWrapper
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import pl.macia.printinghouse.request.BindingFormChangeReq
import pl.macia.printinghouse.request.BindingFormReq
import pl.macia.printinghouse.response.BindingFormResp
import pl.macia.printinghouse.web.dao.BindingFormDao


@Serializable
data class BindingFormSummary(
    val id: Int,
    var name: String
)

fun BindingFormResp.toSummary(): BindingFormSummary {
    return BindingFormSummary(id, name)
}

class BindingFormTab(bindingForms: List<BindingFormResp>, dao: BindingFormDao) : SimplePanel() {
    init {
        val chosenValue = ObservableValue<BindingFormSummary?>(null)
        val summaryList = ObservableListWrapper<BindingFormSummary>()
        bindingForms.forEach {
            summaryList.add(it.toSummary())
        }
        var bindingFormPanel = BindingFormFormPanel {
            chosenValue.subscribe {
                text.value = it?.name
            }
        }
        insertUpdateTable(
            summaryList = summaryList,
            columnsDef = listOf(
                ColumnDefinition("Name", "name")
            ),
            onSelected = {
                chosenValue.value = it //todo every time i create the same value maybe move it somewhere else
            },
            formPanel = {
                bindingFormPanel = BindingFormFormPanel {
                    chosenValue.subscribe {
                        text.value = it?.name
                    }
                }
                bindingFormPanel
            },
            onInsert = {
                val chosen = bindingFormPanel.text.value
                if (chosen != null) {
                    dao.insertBindingForm(
                        BindingFormReq(
                            chosen
                        ),
                        {
                            summaryList.add(BindingFormSummary(it.asInt(), chosen))
                        },
                        {}
                    )
                }
            },
            onUpdate = {
                val chosen = chosenValue.value
                val insertedName = bindingFormPanel.text.value
                if (chosen != null && insertedName != null) {
                    dao.changeBindingForm(
                        chosen.id,
                        BindingFormChangeReq(insertedName),
                        {
                            val selectedIndex = summaryList.indexOf(chosen)
                            summaryList[selectedIndex] = BindingFormSummary(chosen.id, insertedName)
                        },
                        {}
                    )
                }
            }, serializer = serializer()
        )
    }
}

class BindingFormFormPanel(init: (BindingFormFormPanel.() -> Unit)? = null) : SimplePanel() {
    var text = TextInput("Name")

    init {
        add(text)
        init?.invoke(this)
    }
}