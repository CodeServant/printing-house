package pl.macia.printinghouse.web.cli

import io.kvision.form.select.TomSelect
import io.kvision.form.select.TomSelectCallbacks
import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableValue
import io.kvision.state.bind
import io.kvision.tabulator.ColumnDefinition
import io.kvision.utils.obj
import kotlinx.serialization.Serializable
import pl.macia.printinghouse.response.OrderResp
import pl.macia.printinghouse.response.WorkerResp
import pl.macia.printinghouse.response.summary
import pl.macia.printinghouse.web.dao.WorkerDao

@Serializable
data class OrderForWorkflowData(
    val stageCreated: String,
    val client: String,
    val orderName: String
)

/**
 * This is tab for the Workflow Stage manager with unassigned tasks for their workflow stage.
 */
class OrdersToAssignTab(initialOrders: List<OrderResp>, currentWorker: ObservableValue<WorkerResp?>) : SimplePanel() {
    init {
        bind(currentWorker) { workerResp ->
            val workflowstages = workerResp?.isManagerOf?.map { it.name }
            insertUpdateTable(
                summaryList = initialOrders.map {

                    val prevWss = it.workflowStageStops.first { wws ->
                        workflowstages?.contains(wws.graphEdge.v1.name) ?: false
                    }
                    OrderForWorkflowData(
                        stageCreated = prevWss.createTime.toString(),
                        client = it.client.summary(),
                        orderName = it.name
                    )
                },
                columnsDef = listOf(
                    ColumnDefinition("Waiting From", OrderForWorkflowData::stageCreated.name),
                    ColumnDefinition("Client", OrderForWorkflowData::client.name),
                    ColumnDefinition("Order Name", OrderForWorkflowData::orderName.name),
                ),
                onSelected = {},
                onlyEdit = true,
                formPanel = {
                    TomSelect(label = "assignee",
                        tsCallbacks = TomSelectCallbacks(
                            load = { queryStr, callback ->
                                WorkerDao().searchWorker(queryStr, { workers ->
                                    callback(
                                        workers.map {
                                            obj {
                                                this.value = it.id
                                                this.text = "${it.name} ${it.surname} (${it.psudoPESEL})"
                                            }
                                        }.toTypedArray()
                                    )
                                }, {
                                    TODO("exception handling when workers not fetched properly")
                                })
                            }
                        )
                    )
                }
            )
        }
    }
}