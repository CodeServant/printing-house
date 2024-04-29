package pl.macia.printinghouse.web.cli.salesman

import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import pl.macia.printinghouse.response.OrderResp
import pl.macia.printinghouse.web.cli.insertUpdateTable

class FinalizeOrderPanel(ordersToFinalize: List<OrderResp>, init: (FinalizeOrderPanel.() -> Unit)? = null) :
    SimplePanel() {
    val selected = ObservableValue<OrderResp?>(null)

    init {
        insertUpdateTable(
            summaryList = ordersToFinalize,
            columnsDef = listOf(
                ColumnDefinition("id", OrderResp::id.name),
                ColumnDefinition("name", OrderResp::name.name),
                ColumnDefinition("created", OrderResp::creationDate.name),
            ),
            onSelected = {
                selected.value = it
            },
            onUpdate = {
                TODO("send request to finalize order")
            }
        )
        init?.invoke(this)
    }
}