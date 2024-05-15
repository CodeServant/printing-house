package pl.macia.printinghouse.web.cli

import io.kvision.core.AlignItems
import io.kvision.form.ValidationStatus
import io.kvision.form.select.TomSelect
import io.kvision.form.select.TomSelectCallbacks
import io.kvision.panel.HPanel
import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableListWrapper
import io.kvision.state.ObservableValue
import io.kvision.tabulator.ColumnDefinition
import io.kvision.utils.obj
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import pl.macia.printinghouse.request.WorkflowEdgeReq
import pl.macia.printinghouse.request.WorkflowGraphReq
import pl.macia.printinghouse.response.WorkflowGraphResp
import pl.macia.printinghouse.web.dao.WorkflowGraphDao

@Serializable
private data class WorkflowDirGraphSummary(
    var name: String,
    var comment: String?,
    var creationTime: LocalDateTime,
    var edgesSize: Int,
    val id: Int
)

class WorkflowDirGraphTab(workflowGraphResps: List<WorkflowGraphResp>) : SimplePanel() {
    private val worflowGraphSummary = ObservableListWrapper(workflowGraphResps.map {
        WorkflowDirGraphSummary(
            it.name,
            it.comment,
            it.creationTime,
            it.edges.size,
            it.id
        )
    }.toMutableList())

    init {
        val selected = ObservableValue<WorkflowDirGraphSummary?>(null)
        var graphPanel = WorkflowDirGraphForm()
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
                // todo download workflow stages and inject them to input fields
            },
            formPanel = {
                graphPanel = WorkflowDirGraphForm()
                graphPanel
            }, onInsert = {
                val insertedGraphData = graphPanel.getData(true)
                if (insertedGraphData != null) {
                    val toInsert = insertedGraphData.toGraphReq()
                    WorkflowGraphDao().newWorkflowGraph(
                        toInsert,
                        onFulfilled = {
                            insertToast("graph inserted successfully")
                            worflowGraphSummary.add(
                                WorkflowDirGraphSummary(
                                    toInsert.name,
                                    toInsert.comment,
                                    creationTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                                    toInsert.edges.size,
                                    it.id.toInt()
                                )
                            )
                        },
                        onRejected = {
                            TODO("on rejected when manager inserts workflow graph")
                        }
                    )
                }
            },
            onUpdate = {}
        )
    }
}

@Serializable
data class GraphFormData(
    val name: String,
    val comment: String?,
    val edges: List<Pair<Int, Int>>
)

fun GraphFormData.toGraphReq(): WorkflowGraphReq {
    return WorkflowGraphReq(
        name = name,
        comment = comment,
        edges = edges.map {
            WorkflowEdgeReq(it.first, it.second)
        }
    )
}

class WorkflowDirGraphForm : SimplePanel() {
    private var name = TextInput("name")
    private var comment = TextInput("comment")
    private val multipleEdgesPanel = SimplePanel {
        add(WorkflowDirEdge(required = true))
    }

    init {
        add(name)
        add(comment)
        add(multipleEdgesPanel)
        addButton {
            onClick {
                multipleEdgesPanel.add(WorkflowDirEdge(required = true))
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

    fun getData(markFields: Boolean): GraphFormData? {
        if (validate(markFields)) {
            val insName = name.value ?: throw RuntimeException("name should be present")
            val insComment = if (comment.value.isNullOrBlank()) null else comment.value
            val edges = (multipleEdgesPanel.getChildren()).map {
                if (it is WorkflowDirEdge) {
                    it.getData(false)
                        ?: throw RuntimeException("${WorkflowDirEdge::class.simpleName} not properly validated")
                } else throw RuntimeException("all elements in a parent Component should be of a class ${WorkflowDirEdge::class.simpleName}")
            }
            return GraphFormData(
                insName, insComment, edges
            )
        }
        return null
    }

    fun validate(markFields: Boolean): Boolean {
        var retBool = true
        multipleEdgesPanel.getChildren().forEach {
            retBool = (it as WorkflowDirEdge).validate(markFields) && retBool
        }
        val nameIsNB = name.value.isNullOrBlank()
        retBool = retBool && !nameIsNB
        if (nameIsNB)
            name.validatorError = "field is required"
        else
            name.validationStatus = ValidationStatus.VALID
        val cmIsNB = comment.value?.isBlank() ?: false
        retBool = retBool && !cmIsNB
        if (cmIsNB)
            comment.value = null
        return retBool
    }
}

class WorkflowDirEdge(val required: Boolean) : HPanel() {
    private val v1 = WorkflowStagePicker(label = "vertex 1", required = true, maxItems = 1)
    private val v2 = WorkflowStagePicker(label = "vertex 2", required = true, maxItems = 1)

    init {
        alignItems = AlignItems.CENTER
        add(v1)
        add(v2)
    }

    fun getData(markFields: Boolean): Pair<Int, Int>? {
        if (validate(markFields)) {
            val v1 = v1.getData(markFields = true) ?: throw RuntimeException("vertex required")
            val v2 = v2.getData(markFields = true) ?: throw RuntimeException("vertex required")
            v1.firstOrNull() ?: throw RuntimeException("vertex required")
            v2.firstOrNull() ?: throw RuntimeException("vertex required")
            return v1.first() to v2.first()
        }
        return null
    }

    fun validate(markFields: Boolean): Boolean {
        val v1IsNull = v1.getData(markFields = markFields) != null
        val v2IsNull = v2.getData(markFields = markFields) != null
        val arePresent = v1IsNull && v2IsNull
        if (required)
            return arePresent
        return true
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