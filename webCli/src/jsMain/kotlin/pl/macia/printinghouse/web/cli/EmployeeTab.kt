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
import io.kvision.routing.Routing
import io.kvision.state.ObservableValue
import io.kvision.state.bind
import io.kvision.tabulator.ColumnDefinition
import kotlinx.serialization.Serializable
import pl.macia.printinghouse.request.SalesmanChangeReq
import pl.macia.printinghouse.request.SalesmanReq
import pl.macia.printinghouse.request.WorkerChangeReq
import pl.macia.printinghouse.request.WorkerReq
import pl.macia.printinghouse.response.SalesmanResp
import pl.macia.printinghouse.response.WorkerResp
import pl.macia.printinghouse.web.dao.SalesmanDao
import pl.macia.printinghouse.web.dao.WorkerDao
import kotlin.Boolean
import kotlin.Int
import kotlin.collections.List
import kotlin.reflect.KProperty1

enum class EmplType {
    WORKER, SALESMAN, INSERT
}

@Serializable
data class EmployeeSummary(
    var id: Int,
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
    val routing = Routing.init("employees")
        .on({ _ ->
            currentPicked.value = null
        }).on("new-employee", { _ ->
            currentPicked.value = EmployeeSummary(-1, "", "", "", "", EmplType.INSERT)
        }).on("edit-employee", { _ -> })

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
                        routing.navigate("new-employee")
                    }
                )
            } else if (it.type == EmplType.WORKER) {
                routing.navigate("edit-employee")
                val workerInput = WorkerInputPanel(passwordRequired = false)
                WorkerDao().getWorker(
                    it.id,
                    onFulfilled = {
                        workerInput.setData(it.toWorkerInData())
                        controllButtons {
                            val workerChangedData = workerInput.getData(true)
                            if (workerChangedData != null) {
                                WorkerDao().changeWorker(
                                    it.id,
                                    WorkerChangeReq(
                                        nullingRest = false,
                                        workerChangedData.isManagerOf,
                                        workerChangedData.empData.employed,
                                        workerChangedData.empData.activeAccount,
                                        workerChangedData.empData.password,
                                        workerChangedData.empData.email,
                                        workerChangedData.empData.personData.pesel,
                                        workerChangedData.empData.personData.surname,
                                        workerChangedData.empData.personData.name
                                    ),
                                    onFulfilled = {
                                        if(it.changed)
                                            updateToast("Worker data changed")
                                        else {
                                            failToast("There was no change to data", "No change")
                                        }
                                    },
                                    onRejected = {
                                        failToast("Error changing worker data", "Change error")
                                    }
                                )
                            }
                        }
                    },
                    onRejected = {
                        failToast(
                            "no employee found with provided id: ${it.message}. \n Possible wrong Client implementation.",
                            "Employee not found"
                        )
                    }
                )
                add(workerInput)
            } else if (it.type == EmplType.SALESMAN) {
                routing.navigate("edit-employee")
                val salesmanInput = SalesmanInputPanel(passwordRequired = false)
                SalesmanDao().getSalesman(
                    it.id,
                    onFulfilled = {
                        salesmanInput.setData(it.toSalInData())
                        controllButtons {
                            val salesmanChangedData = salesmanInput.getData(true)
                            if (salesmanChangedData != null) {
                                SalesmanDao().changeSalesman(
                                    it.id,
                                    SalesmanChangeReq(
                                        nullingRest = false,
                                        salesmanChangedData.empData.employed,
                                        salesmanChangedData.empData.activeAccount,
                                        salesmanChangedData.empData.password,
                                        salesmanChangedData.empData.email,
                                        salesmanChangedData.empData.personData.pesel,
                                        salesmanChangedData.empData.personData.surname,
                                        salesmanChangedData.empData.personData.name
                                    ),
                                    onFulfilled = {
                                        if(it.changed)
                                            updateToast("Salesman data changed")
                                        else {
                                            failToast("There was no change to data", "No change")
                                        }
                                    },
                                    onRejected = {
                                        failToast("Error changing salesman data", "Change error")
                                    }
                                )
                            }
                        }
                    },
                    onRejected = {
                        failToast(
                            "no employee found with provided id: ${it.message}. \n Possible wrong Client implementation.",
                            "Employee not found"
                        )
                    }
                )
                add(salesmanInput)
            } else if (it.type == EmplType.INSERT) {
                add(GenericEmployeeInput())
            }
        }
    }

    fun Container.controllButtons(onAccept: () -> Unit) {
        hPanel {
            acceptButton() {
                onClick {
                    onAccept()
                }
            }
            cancelButton() {
                onClick {
                    routing.navigate("employees")
                }
            }
        }
    }
}

data class WorkerInputData(
    val isManagerOf: List<Int>,
    val empData: EmployeeInputData
)

private fun WorkerResp.toWorkerInData(): WorkerInputData {

    return WorkerInputData(
        isManagerOf = this.isManagerOf.map { it.id },
        empData = EmployeeInputData(
            employed = this.employed,
            activeAccount = this.activeAccount,
            email = this.email,
            personData = PersonInputData(
                name = this.name,
                surname = this.surname,
                pesel = this.psudoPESEL
            ),
            password = ""
        )
    )
}

class WorkerInputPanel(passwordRequired: Boolean = true) : SimplePanel() {
    val workflowStagePicker = WorkflowStagePicker(
        label = "is manager of",
        required = false
    )
    val empInPanel = EmployeeInputPanel(passwordRequired = passwordRequired)

    init {
        add(workflowStagePicker)
        add(empInPanel)
    }

    fun getData(markFields: Boolean): WorkerInputData? {
        val personData = empInPanel.getData(markFields)
        val managersIds = workflowStagePicker.getData(markFields)
        if (personData == null) return null
        return WorkerInputData(
            isManagerOf = managersIds ?: listOf(),
            empData = personData
        )
    }

    fun setData(initialData: WorkerInputData) {
        empInPanel.setData(initialData.empData)
        workflowStagePicker.setData(initialData.isManagerOf)
    }
}

data class SalesmanInputData(
    val empData: EmployeeInputData
)

private fun SalesmanResp.toSalInData(): SalesmanInputData {
    return SalesmanInputData(
        EmployeeInputData(
            employed = this.employed,
            activeAccount = this.activeAccount,
            email = this.email,
            personData = PersonInputData(
                name = this.name,
                surname = this.surname,
                pesel = this.psudoPESEL
            ),
            password = ""
        )
    )
}

class SalesmanInputPanel(initialData: SalesmanInputData? = null, passwordRequired: Boolean = true) : SimplePanel() {
    val empPanel = EmployeeInputPanel(passwordRequired = passwordRequired)

    init {
        add(empPanel)
        if (initialData != null) {
            setData(initialData)
        }
    }

    fun getData(markFields: Boolean): SalesmanInputData? {
        val data = empPanel.getData(markFields)
        if (data == null) {
            return null
        } else {
            return SalesmanInputData(data)
        }
    }

    fun setData(initialData: SalesmanInputData) {
        empPanel.setData(initialData.empData)
    }
}

data class EmployeeInputData(
    val employed: Boolean,
    val activeAccount: Boolean,
    val email: String,
    val personData: PersonInputData,
    val password: String?
)

class EmployeeInputPanel(initialData: EmployeeInputData? = null, passwordRequired: Boolean = true) : SimplePanel() {
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
        form.add(EmployeeInputData::password, Password(label = "password"), required = passwordRequired)
        add(form)
        if (initialData != null) {
            setData(initialData)
        }
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
            password = form[EmployeeInputData::password]
        )
    }

    fun setData(initialData: EmployeeInputData) {
        personInput.setData(initialData.personData)
        (form.getControl(EmployeeInputData::employed) as CheckBox).value = initialData.employed
        (form.getControl(EmployeeInputData::activeAccount) as CheckBox).value = initialData.activeAccount
        (form.getControl(EmployeeInputData::email) as TextInput).value = initialData.email
    }
}

data class PersonInputData(
    val name: String,
    val surname: String,
    val pesel: String,
)

class PersonInputPanel(initialData: PersonInputData? = null) : SimplePanel() {
    val form = FormPanel<PersonInputData>()

    init {
        val nameIn = TextInput("name")
        val surnameIn = TextInput("surname")
        val peselIn = TextInput("pesel")
        form.add(PersonInputData::name, nameIn, required = true)
        form.add(PersonInputData::surname, surnameIn, required = true)
        form.add(PersonInputData::pesel, peselIn, required = true)
        if (initialData != null) {
            setData(initialData)
        }
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

    fun setData(initialData: PersonInputData) {
        (form.getControl(PersonInputData::name) as TextInput).value = initialData.name
        (form.getControl(PersonInputData::surname) as TextInput).value = initialData.surname
        (form.getControl(PersonInputData::pesel) as TextInput).value = initialData.pesel
    }
}

class GenericEmployeeInput : SimplePanel() {
    init {
        val empType = select(
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
                                        data.empData.password
                                            ?: throw NullPointerException("emp data requires password"),
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
                                        data.empData.password
                                            ?: throw NullPointerException("emp data requires password"),
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
