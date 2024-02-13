package pl.macia.printinghouse.web.cli

import io.kvision.html.button
import io.kvision.i18n.gettext
import io.kvision.i18n.tr
import io.kvision.panel.SimplePanel
import io.kvision.panel.vPanel
import io.kvision.state.ObservableValue
import io.kvision.state.bind
import kotlinx.browser.document
import kotlinx.browser.localStorage
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.web.StorageInfo
import pl.macia.printinghouse.web.dao.BinderyDao
import pl.macia.printinghouse.web.dao.BindingFormDao

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
                        {
                            add(BindingFormTab(it, bindingFormDao))
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

class SalesmanMenu : EmpMenu() {
    init {
        document.title = gettext("Salesman Menu")
        vPanel {
            button("first link")
            button("second link")
            button("third link")
        }
    }
}

class WorkerMenu : EmpMenu() {
    init {
        document.title = gettext("Worker Menu")
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
            PrimaryRoles.WORKER in roles -> add(WorkerMenu())
            PrimaryRoles.WORKFLOW_STAGE_MANAGER in roles -> add(WorkflowManagerMenu())
        }
    }
}