package pl.macia.printinghouse.web.cli

import io.kvision.form.FormPanel
import io.kvision.form.select.TomSelect
import io.kvision.form.select.TomSelectCallbacks
import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableListWrapper
import io.kvision.state.ObservableValue
import io.kvision.state.bind
import io.kvision.tabulator.ColumnDefinition
import io.kvision.utils.obj
import kotlinx.serialization.Serializable
import pl.macia.printinghouse.request.WorkflowStageReq
import pl.macia.printinghouse.response.WorkflowStageResp
import pl.macia.printinghouse.web.dao.WorkerDao
import pl.macia.printinghouse.web.dao.WorkflowStageDao

class WorkflowStageTab(workflowStages: List<WorkflowStageResp>) : SimplePanel() {
    private val form = FormPanel<WorkflowStageInputData>()

    @Serializable
    private data class WorkflowStageSummary(
        val name: String,
        val id: Int,
    )

    private data class WorkflowStageInputData(
        val name: String,
        val workersIds: String
    )

    init {
        val summary = ObservableListWrapper(workflowStages.map {
            WorkflowStageSummary(it.name, it.id)
        }.toMutableList())
        val selected: ObservableValue<WorkflowStageSummary?> = ObservableValue(null)
        var select = TomSelect()
        insertUpdateTable(
            summaryList = summary,
            columnsDef = listOf(
                ColumnDefinition("name", "name"),
            ),
            onSelected = {
                selected.value = it
            },
            formPanel = {
                SimplePanel {
                    val name = TextInput("name").bind(selected) {
                        value = it?.name
                    }
                    select = TomSelect(
                        tsCallbacks = TomSelectCallbacks(
                            load = { query, callback ->
                                WorkerDao().searchWorker(
                                    query,
                                    onFulfilled = { fetched ->
                                        callback(
                                            fetched.map {
                                                obj {
                                                    this.text = it.name
                                                    this.value = it.id
                                                }
                                            }.toTypedArray()
                                        )
                                    },
                                    onRejected = {
                                        TODO()
                                    }
                                )
                            }
                        ),
                        multiple = true,
                        emptyOption = false
                    )
                    form.add(WorkflowStageInputData::name, name, required = true)
                    form.add(WorkflowStageInputData::workersIds, select)
                    this.add(form)
                }
            }, onInsert = {
                if (form.validate(true)) {
                    val insertedName = form[WorkflowStageInputData::name]
                        ?: throw RuntimeException("${WorkflowStageResp::name.name} should is required")
                    val insertedIds = form[WorkflowStageInputData::workersIds].let { idsStr ->
                        idsStr?.split(",")?.map {
                            it.trim().toInt()
                        } ?: listOf()
                    }
                    WorkflowStageDao().newWorkflowStageReq(
                        WorkflowStageReq(
                            name = insertedName,
                            managers = insertedIds
                        ),
                        onFulfilled = {
                            insertToast("workflow stage inserted succesfully")
                            summary.add(
                                WorkflowStageSummary(
                                    insertedName, it.id.toInt()
                                )
                            )
                        },
                        onRejected = {
                            TODO("on rejected when workflow stage not inserted by manager")
                        }
                    )
                }
            }, onUpdate = {}
        )
    }
}
