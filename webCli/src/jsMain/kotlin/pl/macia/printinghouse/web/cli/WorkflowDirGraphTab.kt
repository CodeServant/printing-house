package pl.macia.printinghouse.web.cli

import io.kvision.core.AlignItems
import io.kvision.form.select.TomSelect
import io.kvision.form.select.TomSelectCallbacks
import io.kvision.form.select.tomSelect
import io.kvision.panel.HPanel
import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableList
import io.kvision.state.ObservableListWrapper
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import io.kvision.utils.obj
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import pl.macia.printinghouse.response.WorkflowGraphResp
import pl.macia.printinghouse.web.dao.WorkflowGraphDao

@Serializable
data class WorkflowDirGraphSummary(
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
    var name = TextInput("name")
    var comment = TextInput("comment")
    private var list: ObservableList<WorkflowDirEdge> = ObservableListWrapper()

    init {
        list.add(WorkflowDirEdge())

        val addB = AddButton {
            onClick {
                list.add(WorkflowDirEdge())
            }
        }

        list.subscribe {
            list.forEachIndexed { i, element ->
                if (i == list.size - 1)
                    element.add(addB)
                add(element)
            }
        }
    }
}

class WorkflowDirEdge : HPanel() {
    init {
        alignItems = AlignItems.CENTER
        tomSelect(label = "vertex 1")
        tomSelect(label = "vertex 2")
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