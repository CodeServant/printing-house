package pl.macia.printinghouse.web.cli

import io.kvision.form.ValidationStatus
import io.kvision.form.select.*
import io.kvision.panel.SimplePanel
import io.kvision.utils.obj
import pl.macia.printinghouse.web.dao.WorkflowStageDao

class WorkflowStagePicker(label: String?) : SimplePanel() {
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
                        TODO("on rejected when feting all workflow stages")
                    }
                )
            },
            shouldLoad = { false }
        ),
        tsOptions = TomSelectOptions(
            preload = true
        ), multiple = true
    )

    init {
        add(selectField)
    }

    fun getData(markFields: Boolean): List<Int>? {
        val selected = selectField.value
        if (selected == null) {
            if (markFields)
                selectField.validatorError = "workflow stage not selected"
            return null
        }
        selectField.validationStatus = null
        selectField.validatorError = null
        return try {
            selected.split(",")
                .map { it.toInt() }
        } catch (e: NumberFormatException) {
            null
        }
    }
}