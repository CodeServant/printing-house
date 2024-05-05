package pl.macia.printinghouse.web.cli

import io.kvision.core.Container
import io.kvision.form.FormPanel
import io.kvision.form.check.CheckBox
import io.kvision.form.select.select
import io.kvision.form.text.Password
import io.kvision.html.InputType
import io.kvision.panel.SimplePanel
import io.kvision.panel.hPanel
import io.kvision.panel.simplePanel
import io.kvision.state.ObservableValue
import io.kvision.state.bind
import io.kvision.tabulator.ColumnDefinition
import kotlinx.serialization.Serializable
import pl.macia.printinghouse.request.SalesmanReq
import pl.macia.printinghouse.request.WorkerReq
import pl.macia.printinghouse.web.dao.SalesmanDao
import pl.macia.printinghouse.web.dao.WorkerDao
import kotlin.reflect.KProperty1

enum class EmplType {
    WORKER, SALESMAN, INSERT
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
class EmployeeTab(empResp: List<EmployeeSummary>) : SimplePanel() {
    var currentPicked: ObservableValue<EmployeeSummary?> = ObservableValue(null)

    init {

        simplePanel().bind(currentPicked) {

            if (it == null) {
                insertUpdateTable(
                    summaryList = empResp,
                    columnsDef = listOf(
                        ColumnDefinition("Name", "name"),
                        ColumnDefinition("Surname", "surname"),
                        ColumnDefinition("PESEL", "pesel"),
                        ColumnDefinition("Email", "email"),
                    ),
                    onSelected = {
                        currentPicked.value = it
                    },
                    onInsert = {
                        currentPicked.value = EmployeeSummary("", "", "", "", EmplType.INSERT)
                    }
                )
            } else if (it.type == EmplType.WORKER) {
                add(WorkerInputPanel())
                controllButtons()

            } else if (it.type == EmplType.SALESMAN) {
                add(SalesmanInputPanel())
                controllButtons()
            } else if (it.type == EmplType.INSERT) {
                add(GenericEmployeeInput())
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

data class WorkerInputData(
    val isManagerOf: List<Int>,
    val empData: EmployeeInputData
)

class WorkerInputPanel : SimplePanel() {
    val workflowStagePicker = WorkflowStagePicker(
        label = "is manager of",
        required = false
    )
    val empInPanel = EmployeeInputPanel()

    init {
        add(workflowStagePicker)
        add(empInPanel)
    }

    fun getData(markFields: Boolean): WorkerInputData? {
        val personData = empInPanel.getData(markFields)
        val managersIds = workflowStagePicker.getData(markFields)
        if (personData == null || managersIds == null) return null
        return WorkerInputData(
            isManagerOf = managersIds,
            empData = personData
        )
    }
}

data class SalesmanInputData(
    val empData: EmployeeInputData
)

class SalesmanInputPanel : SimplePanel() {
    val empPanel = EmployeeInputPanel()

    init {
        add(empPanel)
    }

    fun getData(markFields: Boolean): SalesmanInputData? {
        val data = empPanel.getData(markFields)
        if (data == null) {
            return null
        } else {
            return SalesmanInputData(data)
        }
    }
}

data class EmployeeInputData(
    val employed: Boolean,
    val activeAccount: Boolean,
    val email: String,
    val personData: PersonInputData,
    val password: String
)

class EmployeeInputPanel : SimplePanel() {
    val form = FormPanel<EmployeeInputData>()
    val personInput = PersonInputPanel()

    init {
        add(personInput)
        form.add(EmployeeInputData::employed, CheckBox(label = "employed"))
        form.add(EmployeeInputData::activeAccount, CheckBox(label = "active account"))
        val emInput = TextInput(label = "email") {
            type = InputType.EMAIL
        }
        form.add(EmployeeInputData::email, emInput, required = true)
        form.add(EmployeeInputData::password, Password(label = "password"), required = true)
        add(form)
    }

    fun getData(markFields: Boolean): EmployeeInputData? {
        val personData = personInput.getData(markFields)
        if (personData == null || !form.validate(markFields)) return null
        val msg = "data from input not valid"
        return EmployeeInputData(
            employed = form[EmployeeInputData::employed] ?: throw RuntimeException(msg),
            activeAccount = form[EmployeeInputData::activeAccount] ?: throw RuntimeException(msg),
            email = form[EmployeeInputData::email] ?: throw RuntimeException(msg),
            personData = personData,
            password = form[EmployeeInputData::password] ?: throw RuntimeException(msg)
        )
    }
}

data class PersonInputData(
    val name: String,
    val surname: String,
    val pesel: String,
)

class PersonInputPanel : SimplePanel() {
    val form = FormPanel<PersonInputData>()

    init {
        form.add(PersonInputData::name, TextInput("name"), required = true)
        form.add(PersonInputData::surname, TextInput("surname"), required = true)
        form.add(PersonInputData::pesel, TextInput("pesel"), required = true)
        add(form)
    }

    fun getData(markFields: Boolean): PersonInputData? {
        form.validate(markFields)
        fun <T, V> theMassage(prop: KProperty1<T, V>): String {
            return "can't translate input data to valid data format: ${prop}"
        }
        return PersonInputData(
            name = form[PersonInputData::name] ?: throw RuntimeException(theMassage(PersonInputData::name)),
            surname = form[PersonInputData::surname] ?: throw RuntimeException(theMassage(PersonInputData::surname)),
            pesel = form[PersonInputData::pesel] ?: throw RuntimeException(theMassage(PersonInputData::pesel)),
        )
    }
}

class GenericEmployeeInput : SimplePanel() {
    init {
        var empType = select(
            label = "employee type",
            options = listOf(
                Pair("sal", "salesman"),
                Pair("wor", "worker"),
            )
        )
        val salInPanel = SalesmanInputPanel()
        val workInPanel = WorkerInputPanel()
        simplePanel().bind(empType) { pickedEmpl ->
            when (pickedEmpl) {
                "sal" -> {
                    add(salInPanel)
                }

                "wor" -> {
                    add(workInPanel)
                }
            }
        }
        add(
            AddButton {
                onClick {
                    when (empType.value) {
                        "sal" -> {
                            val data = salInPanel.getData(true)
                            if (data != null) {
                                SalesmanDao().newSalesmanReq(
                                    SalesmanReq(
                                        data.empData.employed,
                                        data.empData.activeAccount,
                                        data.empData.password,
                                        data.empData.email,
                                        data.empData.personData.pesel,
                                        data.empData.personData.surname,
                                        data.empData.personData.name,
                                    ), onFulfilled = {
                                        //TODO("fulfilled req for the salesman")
                                    }, onRejected = {
                                        //TODO("rejected req for the salesman")
                                    }
                                )
                            }
                        }

                        "wor" -> {
                            val data = workInPanel.getData(true)
                            if (data != null) {
                                WorkerDao().newWorkerReq(
                                    WorkerReq(
                                        data.isManagerOf,
                                        data.empData.employed,
                                        data.empData.activeAccount,
                                        data.empData.password,
                                        data.empData.email,
                                        data.empData.personData.pesel,
                                        data.empData.personData.surname,
                                        data.empData.personData.name,
                                    ), onFulfilled = {
                                        //TODO("fulfilled req for the worker")
                                    }, onRejected = {
                                        //TODO("rejected req for the worker")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}
