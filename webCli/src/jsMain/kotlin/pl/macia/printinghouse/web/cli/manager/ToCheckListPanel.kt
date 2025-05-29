package pl.macia.printinghouse.web.cli.manager

import io.kvision.html.p
import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableListWrapper
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import kotlinx.datetime.format
import pl.macia.printinghouse.response.OrderResp
import pl.macia.printinghouse.web.cli.failToast
import pl.macia.printinghouse.web.cli.insertUpdateTable
import pl.macia.printinghouse.web.cli.updateToast
import pl.macia.printinghouse.web.clientDateFormat
import pl.macia.printinghouse.web.dao.OrderDao

class ToCheckListPanel(ordersToAccept: List<OrderResp>, init: (ToCheckListPanel.() -> Unit)? = null) :
    SimplePanel() {
    val selected = ObservableValue<OrderResp?>(null)
    val tableList = ObservableListWrapper(ordersToAccept.toMutableList())

    init {
        insertUpdateTable(
            summaryList = tableList,
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
                val picked = selected.value
                if (picked?.id != null) {
                    OrderDao().markAsChecked(
                        picked.id,
                        onFulfilled = {
                            tableList.remove(picked)
                            updateToast("order marked as checked")
                        },
                        onRejected = {
                            failToast("not been able to mark as checked", "checked")
                        }
                    )
                }
            },
            editButtonText = "accept order"
        )
        init?.invoke(this)
    }
}