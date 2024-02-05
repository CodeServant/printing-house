package pl.macia.printinghouse.web.cli

import io.kvision.core.Component
import io.kvision.core.onEvent
import io.kvision.panel.SimplePanel
import io.kvision.panel.hPanel
import io.kvision.state.ObservableValue
import io.kvision.state.bind
import io.kvision.tabulator.ColumnDefinition
import io.kvision.tabulator.Layout
import io.kvision.tabulator.TabulatorOptions
import io.kvision.tabulator.tabulator
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class TaskSummary(
    var assignedTime: LocalDateTime,
    var client: String,
    var orderName: String
)

class WorkerTasksList : SimplePanel() {
    init {
        val summaryList = listOf(
            TaskSummary(LocalDateTime(2023, 12, 12, 12, 12), "Jan Kowalski", "burleska"),
            TaskSummary(LocalDateTime(2023, 11, 11, 11, 11), "Evil Corp sp. z ł.o.o.", "francheska"),
        )
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

private class WorkerTaskTable(
    summaryList: List<TaskSummary>,
    columnsDef: List<ColumnDefinition<TaskSummary>>,
    onSelected: (TaskSummary?) -> Unit,
    formPanel: () -> Component? = { null }
) : SimplePanel() {
    init {
        val buttonType = ObservableValue(ButtonType.ADD)

        tabulator(
            summaryList, options = TabulatorOptions(
                layout = Layout.FITCOLUMNS,
                columns = columnsDef,
                selectable = 1,
            )
        ) {
            onEvent {
                rowSelectionChangedTabulator = {
                    buttonType.value =
                        if (getSelectedRows().size == 1) ButtonType.EDIT else ButtonType.ADD
                    onSelected(getSelectedData().firstOrNull())
                }
            }
        }
        hPanel(useWrappers = true).bind(buttonType) {
            detailsButton()
        }
    }
}