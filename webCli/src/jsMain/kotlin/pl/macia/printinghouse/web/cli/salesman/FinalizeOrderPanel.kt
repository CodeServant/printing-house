package pl.macia.printinghouse.web.cli.salesman

import io.kvision.html.p
import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import kotlinx.datetime.format
import pl.macia.printinghouse.response.OrderResp
import pl.macia.printinghouse.web.cli.insertUpdateTable
import pl.macia.printinghouse.web.clientDateFormat
import pl.macia.printinghouse.web.dao.OrderDao

class FinalizeOrderPanel(ordersToFinalize: List<OrderResp>, init: (FinalizeOrderPanel.() -> Unit)? = null) :
    SimplePanel() {
    val selected = ObservableValue<OrderResp?>(null)

    init {
        insertUpdateTable(
            summaryList = ordersToFinalize,
            columnsDef = listOf(
                ColumnDefinition("id", OrderResp::id.name),
                ColumnDefinition("name", OrderResp::name.name),
                ColumnDefinition(
                    "created",
                    OrderResp::creationDate.name,
                    formatterComponentFunction = { cell, onRendered, data ->
                        p {
                            +data.creationDate.format(clientDateFormat)
                        }
                    }),
            ),
            onSelected = {
                selected.value = it
            },
            onUpdate = {
                val id = selected.value?.id
                if (id != null) {
                    OrderDao().finalizeOrder(
                        id,
                        {
                            println(it)
                        },
                        {
                            TODO("rejected connection when finalizing order")
                        }
                    )
                }
            }
        )
        init?.invoke(this)
    }
}