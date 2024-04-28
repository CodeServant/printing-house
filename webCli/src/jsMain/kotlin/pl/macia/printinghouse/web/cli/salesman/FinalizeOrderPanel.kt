package pl.macia.printinghouse.web.cli.salesman

import io.kvision.panel.SimplePanel
import io.kvision.tabulator.ColumnDefinition
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import pl.macia.printinghouse.response.OrderResp
import pl.macia.printinghouse.web.cli.insertUpdateTable

@Serializable
private data class SalOrderSummary(
    val orderName: String,
    val orderId: Int,
    val created: LocalDateTime
)

class FinalizeOrderPanel(ordersToFinalize: List<OrderResp>, init: (FinalizeOrderPanel.() -> Unit)? = null) :
    SimplePanel() {
    init {
        insertUpdateTable(
            summaryList = ordersToFinalize.map {
                SalOrderSummary(it.name, it.id, it.creationDate)
            },
            columnsDef = listOf(
                ColumnDefinition("id", SalOrderSummary::orderId.name),
                ColumnDefinition("name", SalOrderSummary::orderName.name),
                ColumnDefinition("created", SalOrderSummary::created.name),
            ),
            onSelected = {},
            serializer = serializer(),
            onUpdate = {}
        )
        init?.invoke(this)
    }
}