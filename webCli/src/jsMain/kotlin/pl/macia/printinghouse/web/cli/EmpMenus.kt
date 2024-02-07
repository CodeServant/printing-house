package pl.macia.printinghouse.web.cli

import io.kvision.html.button
import io.kvision.i18n.gettext
import io.kvision.panel.SimplePanel
import io.kvision.panel.vPanel
import kotlinx.browser.document
import kotlinx.browser.localStorage
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.web.StorageInfo

class ManagerMenu : EmpMenu() {
    init {
        document.title = gettext("Managers Menu")
        vPanel {
            button("binderies")
            button("binding forms")
            button("colourings")
            button("employees")
            button("enoblings")
            button("paper types")
            button("printers")
            button("size")
            button("workflow stages")
            button("workflow dir graph")
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