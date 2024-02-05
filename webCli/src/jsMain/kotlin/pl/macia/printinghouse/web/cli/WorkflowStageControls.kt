package pl.macia.printinghouse.web.cli

import io.kvision.form.select.select
import io.kvision.panel.SimplePanel

class WorkflowStagePicker(label: String?) : SimplePanel() {
    init {
        select(
            label = label,
            emptyOption = true,
            options = listOf(
                Pair("exposure-room", "Exposure Room"),
                Pair("bindery", "Bindery"),
                Pair("packaging", "Packaging")
            ),
            rich = true
        )
    }
}