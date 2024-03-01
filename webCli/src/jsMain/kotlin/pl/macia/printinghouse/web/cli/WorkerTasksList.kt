package pl.macia.printinghouse.web.cli

import io.kvision.core.onEvent
import io.kvision.panel.SimplePanel
import io.kvision.panel.hPanel
import io.kvision.state.ObservableListWrapper
import io.kvision.tabulator.ColumnDefinition
import io.kvision.tabulator.Layout
import io.kvision.tabulator.TabulatorOptions
import io.kvision.tabulator.tabulator
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import pl.macia.printinghouse.response.ClientResp
import pl.macia.printinghouse.response.CompanyClientResp
import pl.macia.printinghouse.response.IndividualClientResp
import pl.macia.printinghouse.response.OrderResp

@Serializable
data class TaskSummary(
    val orderId: Int,
    val assignedTime: LocalDateTime,
    val client: String,
    val orderName: String
)

class WorkerTasksList(initialList: List<OrderResp>, onOrderPick: (OrderResp) -> Unit) : SimplePanel() {
    init {
        val summaryList = ObservableListWrapper<TaskSummary>()
        initialList.forEach {
            summaryList.add(it.toTaskSummary())
        }
        val table = WorkerTaskTable(
            summaryList = summaryList,
            columnsDef = listOf(
                ColumnDefinition("assignedTime", "assignedTime"),
                ColumnDefinition("client", "client"),
                ColumnDefinition("orderName", "orderName"),
            ),
            onOrderPick = {
                val id = it.orderId
                val pickedResp = initialList.find {
                    it.id == id
                }
                    ?: throw Exception("there was no such ${OrderResp::class.simpleName} in provided list with the given id")
                onOrderPick(pickedResp)
            }
        )
        add(table)
    }
}

private fun OrderResp.toTaskSummary(): TaskSummary {
    val assignedTime = workflowStageStops.filter {
        it.assignTime != null
    }.maxBy {
        it.assignTime!!
    }.assignTime!!

    val clientLabel: String =
        when (client) {
            is CompanyClientResp -> {
                val client = client as CompanyClientResp
                client.name
            }

            is IndividualClientResp -> {
                val client = client as IndividualClientResp
                "${client.name} ${client.surname}"
            }

            else -> {
                throw Exception("not properly formatted ${ClientResp::class.simpleName}")
            }
        }



    return TaskSummary(
        orderId = id,
        assignedTime,
        orderName = name,
        client = clientLabel
    )
}

private class WorkerTaskTable(
    summaryList: List<TaskSummary>,
    columnsDef: List<ColumnDefinition<TaskSummary>>,
    onOrderPick: (TaskSummary) -> Unit
) : SimplePanel() {
    init {
        var picked: TaskSummary? = null
        tabulator(
            summaryList, options = TabulatorOptions(
                layout = Layout.FITCOLUMNS,
                columns = columnsDef,
                selectableRows = 1,
            )
        ) {
            onEvent {
                rowSelectionChangedTabulator = {
                    picked = getSelectedData().firstOrNull()
                }
            }
        }
        hPanel(useWrappers = true) {
            detailsButton {
                onClick {
                    if (picked != null)
                        onOrderPick(picked!!)
                }
            }
        }
    }
}