package pl.macia.printinghouse.web.cli

import io.kvision.html.Button
import io.kvision.html.button
import io.kvision.i18n.gettext
import io.kvision.i18n.tr
import io.kvision.panel.SimplePanel
import io.kvision.panel.vPanel
import io.kvision.routing.Routing
import io.kvision.state.ObservableListWrapper
import io.kvision.state.ObservableValue
import io.kvision.state.bind
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.browser.window
import pl.macia.printinghouse.response.OrderResp
import pl.macia.printinghouse.response.WorkerResp
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.web.StorageInfo
import pl.macia.printinghouse.web.cli.manager.ToCheckListPanel
import pl.macia.printinghouse.web.cli.salesman.FinalizeOrderPanel
import pl.macia.printinghouse.web.dao.*
import pl.macia.printinghouse.web.logout

enum class ManagerMenuScreen(val path: String) {
    BINDERIES("binderies"),
    BINDING_FORMS("binding-forms"),
    ENOBLINGS("enoblings"),
    EMPLOYEES("employees"),
    PAPER_TYPES("paper-types"),
    PRINTERS("printers"),
    SIZES("sizes"),
    WORKFLOW_STAGES("workflow-stages"),
    WORKFLOW_GRAPHS("workflow-graphs"),
    ORDERS_TO_CHECK("orders-to-check"),
}

class ManagerMenu : EmpMenu() {
    init {
        document.title = gettext("Managers Menu")
        val screen = ObservableValue<ManagerMenuScreen?>(null)
        val routing = Routing.init("menu")
            .on(ManagerMenuScreen.BINDERIES.path, { _ ->
                screen.value = ManagerMenuScreen.BINDERIES
            }).on(ManagerMenuScreen.BINDING_FORMS.path, { _ ->
                screen.value = ManagerMenuScreen.BINDING_FORMS
            }).on(ManagerMenuScreen.EMPLOYEES.path, { _ ->
                screen.value = ManagerMenuScreen.EMPLOYEES
            }).on(ManagerMenuScreen.ENOBLINGS.path, { _ ->
                screen.value = ManagerMenuScreen.ENOBLINGS
            }).on(ManagerMenuScreen.PAPER_TYPES.path, { _ ->
                screen.value = ManagerMenuScreen.PAPER_TYPES
            }).on(ManagerMenuScreen.PRINTERS.path, { _ ->
                screen.value = ManagerMenuScreen.PRINTERS
            }).on(ManagerMenuScreen.SIZES.path, { _ ->
                screen.value = ManagerMenuScreen.SIZES
            }).on(ManagerMenuScreen.WORKFLOW_STAGES.path, { _ ->
                screen.value = ManagerMenuScreen.WORKFLOW_STAGES
            }).on(ManagerMenuScreen.WORKFLOW_GRAPHS.path, { _ ->
                screen.value = ManagerMenuScreen.WORKFLOW_GRAPHS
            }).on(ManagerMenuScreen.ORDERS_TO_CHECK.path, { _ ->
                screen.value = ManagerMenuScreen.ORDERS_TO_CHECK
            }).on({ _ ->
                screen.value = null
            })

        fun switchScr(scr: ManagerMenuScreen) {
            routing.navigate(scr.path)
        }
        bind(screen) {
            when (it) {
                null -> {
                    vPanel {
                        button(tr("binderies")) { onClick { switchScr(ManagerMenuScreen.BINDERIES) } }
                        button(tr("binding forms")) { onClick { switchScr(ManagerMenuScreen.BINDING_FORMS) } }
                        button(tr("employees")) { onClick { switchScr(ManagerMenuScreen.EMPLOYEES) } }
                        button(tr("enoblings")) { onClick { switchScr(ManagerMenuScreen.ENOBLINGS) } }
                        button(tr("paper types")) { onClick { switchScr(ManagerMenuScreen.PAPER_TYPES) } }
                        button(tr("printers")) { onClick { switchScr(ManagerMenuScreen.PRINTERS) } }
                        button(tr("sizes")) { onClick { switchScr(ManagerMenuScreen.SIZES) } }
                        button(tr("workflow stages")) { onClick { switchScr(ManagerMenuScreen.WORKFLOW_STAGES) } }
                        button(tr("workflow graphs")) { onClick { switchScr(ManagerMenuScreen.WORKFLOW_GRAPHS) } }
                        button(tr("orders to check")) { onClick { switchScr(ManagerMenuScreen.ORDERS_TO_CHECK) } }
                        logoutButton()
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

                ManagerMenuScreen.EMPLOYEES -> {
                    val fetched = ObservableListWrapper<EmployeeSummary>(mutableListOf())
                    WorkerDao().allWorkers(
                        onFulfilled = { workerResps ->
                            fetched.addAll(
                                workerResps.map { workerResp ->
                                    EmployeeSummary(
                                        workerResp.id,
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
                                        salesmanResp.id,
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

                ManagerMenuScreen.PAPER_TYPES -> {
                    PaperTypeDao().allPaperTypes(
                        onFulfilled = { paperTypesResp ->
                            add(PaperTypeTab(paperTypesResp))
                        },
                        onRejected = {
                            TODO("on rejected when all paper types fetched by manager")
                        }
                    )
                }

                ManagerMenuScreen.PRINTERS -> {
                    PrinterDao().allPrinters(
                        onFulfilled = { printersResp ->
                            add(PrintersTab(printersResp))
                        },
                        onRejected = {
                            TODO("on rejected when all printers fetched by manager")
                        }
                    )
                }

                ManagerMenuScreen.SIZES -> {
                    SizeDao().allNamedSizes(
                        onFulfilled = {
                            add(SizesTab(it))
                        },
                        onRejected = {
                            TODO("on rejected when fetched all sizes by manager")
                        }
                    )
                }

                ManagerMenuScreen.WORKFLOW_STAGES -> {
                    WorkflowStageDao().allWorkflowStages(
                        onFulfilled = {
                            add(WorkflowStageTab(it))
                        },
                        onRejected = {
                            TODO("on rejected when fetched all workflow stages by manager")
                        }
                    )
                }

                ManagerMenuScreen.WORKFLOW_GRAPHS -> {
                    WorkflowGraphDao().allWorkflowGraphs(
                        onFulfilled = {
                            add(WorkflowDirGraphTab(it))
                        },
                        onRejected = {
                            TODO("not fetched by manager all workflow graphs")
                        }
                    )
                }

                ManagerMenuScreen.ENOBLINGS -> {
                    EnoblingDao().allEnoblings(
                        onFulfilled = { enoblingResps ->
                            add(EnoblingTab(enoblingResps))
                        },
                        onRejected = {
                            TODO("on rejected enoblings all fetch")
                        }
                    )
                }

                ManagerMenuScreen.ORDERS_TO_CHECK -> {
                    OrderDao().notCheckedOrders(
                        onFulfilled = {
                            add(ToCheckListPanel(it.data))
                        },
                        onRejected = {
                            routing.navigate("menu")
                        }
                    )
                }
            }
        }
    }
}

private enum class SalesmanMenuScreens(val path: String) {
    INSERT("insertion"), FINALIZE("finalization"), YOUR_ORDERS("your-orders"), MAIN("")
}

class SalesmanMenu : EmpMenu() {
    private var screen = ObservableValue(SalesmanMenuScreens.MAIN)
    private val routing = Routing.init("/salesman")
        .on(SalesmanMenuScreens.INSERT.path, { _ ->
            screen.value = SalesmanMenuScreens.INSERT
        }).on(SalesmanMenuScreens.FINALIZE.path, { _ ->
            screen.value = SalesmanMenuScreens.FINALIZE
        }).on(SalesmanMenuScreens.YOUR_ORDERS.path, { _ ->
            screen.value = SalesmanMenuScreens.YOUR_ORDERS
        }).on(SalesmanMenuScreens.MAIN.path, { _ ->
            screen.value = SalesmanMenuScreens.MAIN
        })

    init {
        document.title = gettext("Salesman Menu")

        val insertButton = Button("insert new order") {
            onClick {
                routing.navigate(SalesmanMenuScreens.INSERT.path)
            }
        }
        val finalizeButton = Button("finalize order") {
            onClick {
                routing.navigate(SalesmanMenuScreens.FINALIZE.path)
            }
        }
        val yourPOrderButton = Button("your orders") {
            onClick {
                routing.navigate(SalesmanMenuScreens.YOUR_ORDERS.path)
            }
        }
        bind(screen) { scr ->
            when (scr) {
                SalesmanMenuScreens.INSERT -> {
                    val salOrderPanel = SalesmanNewOrderPanel(
                        onSave = { TODO() },
                        onLeave = {
                            routing.navigate(SalesmanMenuScreens.MAIN.path)
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
                        logoutButton()
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
                add(
                    WorkerOrderDisplay(
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
        logoutButton()
    }
}

enum class WorkflowMgrMenuOptions(val path: String) {
    MAIN("menu"), ASSIGN_TASKS("assign-tasks")
}

class WorkflowManagerMenu : EmpMenu() {
    init {
        document.title = gettext("Workflow Manager Menu")
        val currentPanel = ObservableValue(WorkflowMgrMenuOptions.MAIN)
        val routing = Routing.init("/workflowManager")
            .on(WorkflowMgrMenuOptions.MAIN.path, { _ ->
                currentPanel.value = WorkflowMgrMenuOptions.MAIN
            }).on(WorkflowMgrMenuOptions.ASSIGN_TASKS.path, { _ ->
                currentPanel.value = WorkflowMgrMenuOptions.ASSIGN_TASKS
            })
        val curWorker = ObservableValue<WorkerResp?>(null)
        val assignTaskButton = Button(text = "assign tasks to worker") {
            onClick {
                routing.navigate(WorkflowMgrMenuOptions.ASSIGN_TASKS.path)
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
                                        routing.navigate(WorkflowMgrMenuOptions.MAIN.path)
                                    }
                                )
                            )
                        },
                        onRejected = {
                            routing.navigate(WorkflowMgrMenuOptions.MAIN.path)
                            TODO("bahaviour when there is an error")
                        },
                    )

                }

                WorkflowMgrMenuOptions.MAIN -> {
                    add(assignTaskButton)
                    logoutButton()
                }
            }
        }
    }
}

open class EmpMenu : SimplePanel()

private fun EmpMenu.logoutButton() {
    add(
        CancelButton("logout") {
            onClick {
                StorageInfo(localStorage).logout()
                window.location.reload()
            }
        }
    )
}

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