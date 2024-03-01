package pl.macia.printinghouse.web.cli

import io.kvision.core.Component
import io.kvision.core.onEvent
import io.kvision.panel.SimplePanel
import io.kvision.panel.hPanel
import io.kvision.state.ObservableListWrapper
import io.kvision.state.ObservableValue
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

class WorkerTasksList(initialList: List<OrderResp>) : SimplePanel() {
    init {
        val summaryList = ObservableListWrapper<TaskSummary>()
        initialList.forEach {
            summaryList.add(it.toTaskSummary())
        }
        val formData = ObservableValue<TaskSummary?>(null)
        val table = WorkerTaskTable(
            summaryList = summaryList,
            columnsDef = listOf(
                ColumnDefinition("assignedTime", "assignedTime"),
                ColumnDefinition("client", "client"),
                ColumnDefinition("orderName", "orderName"),
            ),
            onSelected = {
                formData.value = it
            },
            formPanel = {
                SimplePanel {
                    val assignedTime = textInput("assignedTime")
                    val client = textInput("client")
                    val orderName = textInput("orderName")
                    formData.subscribe {
                        assignedTime.value = it?.assignedTime.toString()
                        client.value = it?.client
                        orderName.value = it?.orderName
                    }
                }
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
    onSelected: (TaskSummary?) -> Unit,
    formPanel: () -> Component? = { null }
) : SimplePanel() {
    init {
        tabulator(
            summaryList, options = TabulatorOptions(
                layout = Layout.FITCOLUMNS,
                columns = columnsDef,
                selectableRows = 1,
            )
        ) {
            onEvent {
                rowSelectionChangedTabulator = {
                    onSelected(getSelectedData().firstOrNull())
                }
            }
        }
        hPanel(useWrappers = true) {
            detailsButton()
        }
    }
}