package pl.macia.printinghouse.web.cli

import io.kvision.html.Button
import io.kvision.html.button
import io.kvision.i18n.gettext
import io.kvision.i18n.tr
import io.kvision.panel.SimplePanel
import io.kvision.panel.vPanel
import io.kvision.state.ObservableValue
import io.kvision.state.bind
import kotlinx.browser.document
import kotlinx.browser.localStorage
import pl.macia.printinghouse.response.OrderResp
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.web.StorageInfo
import pl.macia.printinghouse.web.dao.BinderyDao
import pl.macia.printinghouse.web.dao.BindingFormDao
import pl.macia.printinghouse.web.dao.OrderDao
import pl.macia.printinghouse.web.dao.WorkflowStageStopDao

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
                ManagerMenuScreen.EMPLOYEES -> add(EmployeeTab())
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
                        onSave = TODO(),
                        onLeave = {
                            screen.value = SalesmanMenuScreens.MAIN
                        },
                        onAccept = TODO()
                    )
                    add(salOrderPanel)
                }

                SalesmanMenuScreens.FINALIZE -> TODO()
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

class WorkflowManagerMenu : EmpMenu() {
    init {
        document.title = gettext("Workflow Manager Menu")
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