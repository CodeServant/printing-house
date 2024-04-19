package pl.macia.printinghouse.web.cli

import io.kvision.form.select.TomSelect
import io.kvision.panel.SimplePanel
import io.kvision.tabulator.ColumnDefinition
import kotlinx.serialization.Serializable

@Serializable
data class OrderForWorkflowData(
    val stageCreated: String,
    val client: String,
    val orderName: String
)

/**
 * This is tab for the Workflow Stage manager with unassigned tasks for their workflow stage.
 */
class OrdersToAssignTab : SimplePanel() {
    init {
        insertUpdateTable(
            summaryList = listOf(
                //todo implement fetch from server
            ),
            columnsDef = listOf(
                ColumnDefinition("Waiting From", OrderForWorkflowData::stageCreated.name),
                ColumnDefinition("Client", OrderForWorkflowData::client.name),
                ColumnDefinition("Order Name", OrderForWorkflowData::orderName.name),
            ),
            onSelected = {},
            onlyEdit = true,
            formPanel = {
                TomSelect(label = "assignee")
            }
        )
    }
}