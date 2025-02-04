package pl.macia.printinghouse.web.cli

import io.kvision.form.select.*
import io.kvision.panel.SimplePanel
import io.kvision.utils.obj
import pl.macia.printinghouse.web.dao.WorkflowStageDao

class WorkflowStagePicker(label: String?, private val required: Boolean, val maxItems: Int = -1) : SimplePanel() {
    private val selectField = TomSelect(
        label = label,
        rich = true,
        tsCallbacks = TomSelectCallbacks(
            load = { _, callback ->
                WorkflowStageDao().allWorkflowStages(
                    onFulfilled = { fetched ->
                        callback(
                            fetched.map {
                                obj {
                                    this.text = it.name
                                    this.value = it.id.toString()
                                }
                            }.toTypedArray()
                        )
                    },
                    onRejected = {
                        failToast("couldn't fetch data about managers workflow stages", "workflow stages fetch error")
                    }
                )
            },
            shouldLoad = { false }
        ),
        tsOptions = TomSelectOptions(
            preload = true,
            maxItems = if (maxItems < 1) null else maxItems,
        ), multiple = true
    )

    init {
        add(selectField)
    }

    fun getData(markFields: Boolean): List<Int>? {
        val selected = selectField.value
        if (validate(markFields)) {
            return try {
                selected?.split(",")
                    ?.map { it.toInt() } ?: listOf()
            } catch (e: NumberFormatException) {
                null
            }
        }
        return null
    }

    fun validate(markFields: Boolean): Boolean {
        val selected = selectField.value
        if (selected == null && required) {
            if (markFields)
                selectField.validatorError = "workflow stage not selected"
            return false
        }
        selectField.validationStatus = null
        selectField.validatorError = null
        return true
    }

    fun setData(initData: List<Int>) {
        WorkflowStageDao().allWorkflowStages(
            onFulfilled = { fetched ->
                selectField.options = fetched.map {
                    Pair(it.id.toString(), it.name)
                }
                selectField.value = ""
                if (initData.isNotEmpty()) {
                    for (v in initData.subList(0, initData.size - 1)) {
                        selectField.value += "$v,"
                    }
                    selectField.value += "${initData.last()}"
                }
            },
            onRejected = {
                failToast("couldn't fetch data about managers workflow stages", "workflow stages fetch error")
            }
        )
    }
}