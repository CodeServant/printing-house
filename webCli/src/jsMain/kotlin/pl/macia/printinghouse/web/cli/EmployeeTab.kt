package pl.macia.printinghouse.web.cli

import io.kvision.core.Container
import io.kvision.form.check.checkBox
import io.kvision.html.InputType
import io.kvision.panel.SimplePanel
import io.kvision.panel.hPanel
import io.kvision.panel.simplePanel
import io.kvision.state.ObservableValue
import io.kvision.state.bind
import io.kvision.tabulator.ColumnDefinition
import kotlinx.serialization.Serializable

enum class EmplType {
    WORKER, SALESMAN
}

@Serializable
data class EmployeeSummary(
    var name: String,
    var surname: String,
    var pesel: String,
    var email: String,
    var type: EmplType
)


/**
 * Table containing employee list.
 */
class EmployeeTab : SimplePanel() {
    var currentPicked: ObservableValue<EmployeeSummary?> = ObservableValue(null)

    init {

        simplePanel().bind(currentPicked) {

            if (it == null) {
                insertUpdateTable(
                    summaryList = listOf(
                        EmployeeSummary("Joe", "Biden", "joesomepesl", "joe@example.com", EmplType.WORKER),
                        EmployeeSummary("Donald", "Trump", "trumpmepesl", "trump@example.com", EmplType.SALESMAN),
                        EmployeeSummary("Barack", "Obama", "obamamepesl", "barack@example.com", EmplType.WORKER),
                    ),
                    columnsDef = listOf(
                        ColumnDefinition("Name", "name"),
                        ColumnDefinition("Surname", "surname"),
                        ColumnDefinition("PESEL", "pesel"),
                        ColumnDefinition("Email", "email"),
                    ),
                    onSelected = {
                        currentPicked.value = it


                    }
                )
            } else if (it.type == EmplType.WORKER) {
                add(WorkerInputPanel())
                controllButtons()

            } else if (it.type == EmplType.SALESMAN) {
                add(SalesmanInputPanel())
                controllButtons()
            }
        }
    }

    fun Container.controllButtons() {
        hPanel {
            acceptButton()
            cancelButton() {
                onClick {
                    currentPicked.value = null
                }
            }
        }
    }
}

class WorkerInputPanel : SimplePanel() {
    init {
        add(
            WorkflowStagePicker(
                label = "is manager of"
            )
        )
        add(EmployeeInputPanel())
    }
}

class SalesmanInputPanel : SimplePanel() {
    init {
        add(EmployeeInputPanel())
    }
}

class EmployeeInputPanel : SimplePanel() {
    init {
        add(PersonInputPanel())
        checkBox(label = "employed") { }
        checkBox(label = "active account") { }
        textInput(label = "email") {
            type = InputType.EMAIL
        }
    }
}

class PersonInputPanel : SimplePanel() {
    init {
        textInput("name")
        textInput("surname")
        textInput("pesel")
    }
}

