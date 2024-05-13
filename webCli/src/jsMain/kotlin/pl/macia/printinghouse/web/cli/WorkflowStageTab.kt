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
import pl.macia.printinghouse.request.WorkflowStageChangeReq
import pl.macia.printinghouse.request.WorkflowStageReq
import pl.macia.printinghouse.response.PersonsIdentityResp
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

    private val managersForPicked = ObservableListWrapper<PersonsIdentityResp>()

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
                managersForPicked.clear()
                if (it != null) {
                    WorkflowStageDao().getWorkflowStage(
                        it.id,
                        onFulfilled = {
                            managersForPicked.addAll(it.managers)
                        },
                        onRejected = {
                            TODO("on rejected when there is not workflow stage with the given id")
                        }
                    )
                }
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
                                                    this.text = "${it.name} ${it.surname}"
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
                    select.bind(managersForPicked) {
                        select.options = it.map {
                            it.id.toString() to "${it.name} ${it.surname}"
                        }
                        select.value = it.joinToString(separator = ",", transform = { obj ->
                            obj.id.toString()
                        })
                        select.disabled = selected.value != null
                    }
                    form.add(WorkflowStageInputData::name, name, required = true)
                    form.add(WorkflowStageInputData::workersIds, select)
                    this.add(form)
                }
            }, onInsert = {
                if (form.validate(true)) {
                    val insertedName = form[WorkflowStageInputData::name]
                        ?: throw RuntimeException("${WorkflowStageInputData::name.name} should be required")
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
                            insertToast("workflow stage inserted successfully")
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
            }, onUpdate = {
                val selectedVal = selected.value
                if (form.validate(true) && selectedVal != null) {
                    val insertedName = form[WorkflowStageInputData::name]
                        ?: throw RuntimeException("${WorkflowStageInputData::name.name} should be required")
                    WorkflowStageDao().changeWorkflowStage(
                        selectedVal.id,
                        WorkflowStageChangeReq(insertedName),
                        onFulfilled = {
                            if (it.changed) {
                                updateToast("workflow stage updated successfully")
                                val updatedIndex = summary.indexOf(selectedVal)
                                summary[updatedIndex] = WorkflowStageSummary(insertedName, selectedVal.id)
                            }
                        },
                        onRejected = {
                            TODO("on rejected when workflow stage not changed by manager")
                        }
                    )
                }
            }
        )
    }
}
