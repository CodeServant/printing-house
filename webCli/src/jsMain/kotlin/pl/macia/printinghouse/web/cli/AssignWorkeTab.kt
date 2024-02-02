package pl.macia.printinghouse.web.cli

import io.kvision.form.select.tomSelect
import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class AssignedWorker(
    var name: String,
    var surname: String,
    var pesel: String
)

@Serializable
data class OrderInfo(
    var name: String
)

@Serializable
data class WorkerTaskSummary(
    var prewWorkflowStageName: String,
    var workflowStageName: String,
    var worker: AssignedWorker,
    var createTime: LocalDateTime,
    var orderName: OrderInfo,
)

class AssignWorkeTab() : SimplePanel() {
    init {
        var pickedTask = ObservableValue<WorkerTaskSummary?>(null)
        insertUpdateTable(
            summaryList = listOf(
                WorkerTaskSummary(
                    "Handlowiec",
                    "Introligatornia",
                    AssignedWorker(
                        "Jan",
                        "Kowalski",
                        "120120x2131"
                    ),
                    LocalDateTime(2023, 10, 12, 0, 0),
                    OrderInfo("burleska")
                )
            ),
            columnsDef = listOf(
                ColumnDefinition("Previous workflow stage", "prewWorkflowStageName"),
                ColumnDefinition("Current workflow stage", "workflowStageName"),
                ColumnDefinition("assign time", "assignTime"),
                ColumnDefinition("worker name", "worker.name"),
                ColumnDefinition("worker surname", "worker.surname"),
                ColumnDefinition("worker PESEL", "worker.pesel"),
                ColumnDefinition("createTime", "createTime"),
                ColumnDefinition("order name", "orderName.name"),
            ),
            onSelected = {
                pickedTask.value = it
            },
            formPanel = {
                WorkerTaskSummaryPanel()
            }
        )
    }
}

class WorkerTaskSummaryPanel(
    init: (SimplePanel.() -> Unit)? = null
) :
    SimplePanel() {


    init {
        init?.invoke(this)
        //todo change this data when fetched from REST API
        textInput("comment")
        textInput(label = "assignTime") {
            disabled = true
        }
        textInput("create time") {
            disabled = true
        }
        tomSelect(label = "select worker") {

        }
        textInput(label = "order name") {
            disabled = true
        }
    }
}