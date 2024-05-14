package pl.macia.printinghouse.web.cli

import io.kvision.core.AlignItems
import io.kvision.form.select.TomSelect
import io.kvision.form.select.TomSelectCallbacks
import io.kvision.panel.HPanel
import io.kvision.panel.SimplePanel
import io.kvision.panel.simplePanel
import io.kvision.state.ObservableListWrapper
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import io.kvision.utils.obj
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import pl.macia.printinghouse.response.WorkflowGraphResp
import pl.macia.printinghouse.web.dao.WorkflowGraphDao

@Serializable
private data class WorkflowDirGraphSummary(
    var name: String,
    var comment: String?,
    var creationTime: LocalDateTime,
    var edgesSize: Int
)

class WorkflowDirGraphTab(workflowGraphResps: List<WorkflowGraphResp>) : SimplePanel() {
    private val worflowGraphSummary = ObservableListWrapper(workflowGraphResps.map {
        WorkflowDirGraphSummary(
            it.name,
            it.comment,
            it.creationTime,
            it.edges.size
        )
    }.toMutableList())

    init {
        val selected = ObservableValue<WorkflowDirGraphSummary?>(null)
        insertUpdateTable(
            summaryList = worflowGraphSummary,
            columnsDef = listOf(
                ColumnDefinition("name", WorkflowDirGraphSummary::name.name),
                ColumnDefinition("comment", WorkflowDirGraphSummary::comment.name),
                ColumnDefinition("creation time", WorkflowDirGraphSummary::creationTime.name),
                ColumnDefinition("edge size", WorkflowDirGraphSummary::edgesSize.name),
            ),
            onSelected = {
                selected.value = it
            },
            formPanel = {
                WorkflowDirGraphForm()
            }, onInsert = {},
            onUpdate = {}
        )
    }
}

class WorkflowDirGraphForm : SimplePanel() {
    private var name = TextInput("name")
    private var comment = TextInput("comment")

    init {
        add(name)
        add(comment)
        val multipleEdgesPanel = simplePanel {
            add(WorkflowDirEdge())
        }
        addButton {
            onClick {
                multipleEdgesPanel.add(WorkflowDirEdge())
            }
        }
        val cancel = CancelButton("delete edge") {
            onClick {
                if (multipleEdgesPanel.getChildren().size > 1)
                    multipleEdgesPanel.removeAt(multipleEdgesPanel.getChildren().size - 1)
            }
        }
        add(cancel)
    }
}

class WorkflowDirEdge : HPanel() {
    init {
        alignItems = AlignItems.CENTER
        add(WorkflowStagePicker(label = "vertex 1", required = true, maxItems = 1))
        add(WorkflowStagePicker(label = "vertex 2", required = true, maxItems = 1))
    }
}

class WorkflowGraphSelect : TomSelect(label = "Workflow Graph") {
    init {
        tsCallbacks = TomSelectCallbacks(
            load = { _, callback ->
                WorkflowGraphDao().allWorkflowGraphs(
                    onFulfilled = { graphhs ->
                        val sumUp = graphhs.map {
                            obj {
                                this.value = it.id
                                this.text = it.name
                            }
                        }.toTypedArray()
                        callback(sumUp)
                    },
                    onRejected = {
                        TODO("implement on rejected workflow graph")
                    }
                )
            }
        )
    }
}