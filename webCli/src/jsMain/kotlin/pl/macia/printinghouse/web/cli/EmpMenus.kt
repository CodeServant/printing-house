package pl.macia.printinghouse.web.cli

import io.kvision.html.Button
import io.kvision.html.button
import io.kvision.i18n.gettext
import io.kvision.i18n.tr
import io.kvision.panel.SimplePanel
import io.kvision.panel.vPanel
import io.kvision.state.ObservableListWrapper
import io.kvision.state.ObservableValue
import io.kvision.state.bind
import kotlinx.browser.document
import kotlinx.browser.localStorage
import pl.macia.printinghouse.response.OrderResp
import pl.macia.printinghouse.response.WorkerResp
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.web.StorageInfo
import pl.macia.printinghouse.web.cli.salesman.FinalizeOrderPanel
import pl.macia.printinghouse.web.dao.*

enum class ManagerMenuScreen {
    BINDERIES,
    BINDING_FORMS,
    COLOURINGS,
    ENOBLINGS,
    EMPLOYEES,
    PAPER_TYPES,
    PRINTERS,
    SIZES,
    WORKFLOW_STAGES,
    WORKFLOW_GRAPHS,
}

class ManagerMenu : EmpMenu() {
    init {
        document.title = gettext("Managers Menu")
        val screen = ObservableValue<ManagerMenuScreen?>(null)
        bind(screen) {
            when (it) {
                null -> {
                    fun switchScr(scr: ManagerMenuScreen) {
                        screen.value = scr
                    }
                    vPanel {
                        button(tr("binderies")) { onClick { switchScr(ManagerMenuScreen.BINDERIES) } }
                        button(tr("binding forms")) { onClick { switchScr(ManagerMenuScreen.BINDING_FORMS) } }
                        button(tr("colourings")) { onClick { switchScr(ManagerMenuScreen.COLOURINGS) } }
                        button(tr("employees")) { onClick { switchScr(ManagerMenuScreen.EMPLOYEES) } }
                        button(tr("enoblings")) { onClick { switchScr(ManagerMenuScreen.ENOBLINGS) } }
                        button(tr("paper types")) { onClick { switchScr(ManagerMenuScreen.PAPER_TYPES) } }
                        button(tr("printers")) { onClick { switchScr(ManagerMenuScreen.PRINTERS) } }
                        button(tr("sizes")) { onClick { switchScr(ManagerMenuScreen.SIZES) } }
                        button(tr("workflow stages")) { onClick { switchScr(ManagerMenuScreen.WORKFLOW_STAGES) } }
                        button(tr("workflow graphs")) { onClick { switchScr(ManagerMenuScreen.WORKFLOW_GRAPHS) } }
                    }
                }

                ManagerMenuScreen.BINDERIES -> {
                    val binderyDao = BinderyDao()
                    binderyDao.allBinderies(
                        { binderiesResp ->
                            add(BinderiesTab(binderiesResp, binderyDao))
                        },
                        {/* todo initiate */ }
                    )
                }

                ManagerMenuScreen.BINDING_FORMS -> {
                    val bindingFormDao = BindingFormDao()
                    bindingFormDao.allBindingForms(
                        { responses ->
                            add(BindingFormTab(responses, bindingFormDao))
                        },
                        {}
                    )

                }

                ManagerMenuScreen.COLOURINGS -> add(ColouringTab())
                ManagerMenuScreen.EMPLOYEES -> {
                    val fetched = ObservableListWrapper<EmployeeSummary>(mutableListOf())
                    WorkerDao().allWorkers(
                        onFulfilled = { workerResps ->
                            fetched.addAll(
                                workerResps.map { workerResp ->
                                    EmployeeSummary(
                                        workerResp.name,
                                        workerResp.surname,
                                        workerResp.psudoPESEL,
                                        workerResp.email,
                                        EmplType.WORKER
                                    )
                                }
                            )
                        }, onRejected = {
                            TODO("on rejected when worker list not fetched")
                        }


                    )
                    SalesmanDao().allSalesmans(
                        onFulfilled = { salesmanResps ->
                            fetched.addAll(
                                salesmanResps.map { salesmanResp ->
                                    EmployeeSummary(
                                        salesmanResp.name,
                                        salesmanResp.surname,
                                        salesmanResp.psudoPESEL,
                                        salesmanResp.email,
                                        EmplType.SALESMAN
                                    )
                                }
                            )
                        }, onRejected = {
                            TODO("on rejected when salesmans list not fetched")
                        }
                    )
                    add(EmployeeTab(fetched))
                }
                ManagerMenuScreen.PAPER_TYPES -> add(PaperTypeTab())
                ManagerMenuScreen.PRINTERS -> add(PrintersTab())
                ManagerMenuScreen.SIZES -> add(SizesTab())
                ManagerMenuScreen.WORKFLOW_STAGES -> add(WorkflowStageTab())
                ManagerMenuScreen.WORKFLOW_GRAPHS -> add(WorkflowDirGraphTab())
                ManagerMenuScreen.ENOBLINGS -> add(EnoblingTab())
            }
        }
    }
}

private enum class SalesmanMenuScreens {
    INSERT, FINALIZE, YOUR_ORDERS, MAIN
}

class SalesmanMenu : EmpMenu() {
    private var screen = ObservableValue(SalesmanMenuScreens.MAIN)

    init {
        document.title = gettext("Salesman Menu")

        val insertButton = Button("insert new order") {
            onClick {
                screen.value = SalesmanMenuScreens.INSERT
            }
        }
        val finalizeButton = Button("finalize order") {
            onClick {
                screen.value = SalesmanMenuScreens.FINALIZE
            }
        }
        val yourPOrderButton = Button("your orders") {
            onClick {
                screen.value = SalesmanMenuScreens.YOUR_ORDERS
            }
        }
        bind(screen) { scr ->
            when (scr) {
                SalesmanMenuScreens.INSERT -> {
                    val salOrderPanel = SalesmanNewOrderPanel(
                        onSave = { TODO() },
                        onLeave = {
                            screen.value = SalesmanMenuScreens.MAIN
                        },
                        onAccept = { TODO() }
                    )
                    add(salOrderPanel)
                }

                SalesmanMenuScreens.FINALIZE -> {
                    OrderDao().toFinalizeOrders(
                        {
                            add(FinalizeOrderPanel(it.data))
                        },
                        {
                            TODO("orders to finalize not fetched")
                        }
                    )
                }

                SalesmanMenuScreens.YOUR_ORDERS -> TODO()
                SalesmanMenuScreens.MAIN -> {
                    vPanel {
                        add(insertButton)
                        add(finalizeButton)
                        add(yourPOrderButton)
                    }
                }
            }
        }
    }
}

class WorkerMenu : EmpMenu() {
    private val orderResp = ObservableValue<OrderResp?>(null)

    init {
        document.title = gettext("Worker Menu")
        val orderDao = OrderDao()
        val storageInfo = StorageInfo(localStorage)
        this.bind(orderResp) { resp ->
            if (resp == null) {
                orderDao.getAssigneeseOrders(
                    onFulfilled = { orResList ->
                        add(
                            WorkerTasksList(
                                orResList,
                                onOrderPick = { orRes ->
                                    orderResp.value = orRes
                                }
                            )
                        )
                    },
                    onRejected = {
                        /* todo initialize */
                    }
                )
            } else {
                add(WorkerOrderDisplay(
                    resp,
                    onBack = {
                        orderResp.value = null
                    },
                    onDone = {
                        val wssId = orderResp.value!!.workflowStageStops.filter {
                            it.worker?.email == storageInfo.username && it.assignTime != null
                        }.maxBy {
                            it.assignTime!!
                        }.wfssId!!
                        WorkflowStageStopDao().markWorkflowStageAsDone(
                            wssId,
                            onFulfilled = (::println), //todo make visual responses to user
                            onRejected = (::println)
                        )
                    }
                ))
            }
        }
    }
}

enum class WorkflowMgrMenuOptions {
    MAIN, ASSIGN_TASKS
}

class WorkflowManagerMenu : EmpMenu() {
    init {
        document.title = gettext("Workflow Manager Menu")
        val currentPanel = ObservableValue(WorkflowMgrMenuOptions.MAIN)
        val curWorker = ObservableValue<WorkerResp?>(null)
        val assignTaskButton = Button(text = "assign tasks to worker") {
            onClick {
                currentPanel.value = WorkflowMgrMenuOptions.ASSIGN_TASKS
            }
        }
        WorkerDao().currentWorker(
            onFulfilled = {
                curWorker.value = it
            },
            onRejected = {
                TODO("initiate on rejected when current user is not being fetched")
            },
        )
        this.bind(currentPanel) { workMgrPanel ->
            when (workMgrPanel) {
                WorkflowMgrMenuOptions.ASSIGN_TASKS -> {
                    OrderDao().getUnassigned(
                        onFulfilled = {
                            println("fulfilled worker")
                            add(
                                OrdersToAssignTab(
                                    it,
                                    curWorker,
                                    onLeave = {
                                        currentPanel.value = WorkflowMgrMenuOptions.MAIN
                                    }
                                )
                            )
                        },
                        onRejected = {
                            currentPanel.value = WorkflowMgrMenuOptions.MAIN
                            TODO("bahaviour when there is an error")
                        },
                    )

                }

                WorkflowMgrMenuOptions.MAIN -> {
                    add(assignTaskButton)
                }
            }
        }
    }
}

open class EmpMenu : SimplePanel()

class Menu : SimplePanel() {
    init {
        val storage = StorageInfo(localStorage)
        val roles = storage.userRoles ?: listOf()
        when {
            PrimaryRoles.MANAGER in roles -> add(ManagerMenu())
            PrimaryRoles.SALESMAN in roles -> add(SalesmanMenu())
            PrimaryRoles.WORKFLOW_STAGE_MANAGER in roles -> add(WorkflowManagerMenu())
            PrimaryRoles.WORKER in roles -> add(WorkerMenu())
        }
    }
}