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
            button("first link")
            button("second link")
            button("third link")
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

open class EmpMenu : SimplePanel()

class Menu : SimplePanel() {
    init {
        val storage = StorageInfo(localStorage)
        val roles = storage.userRoles ?: listOf()
        when {
            PrimaryRoles.MANAGER in roles -> add(ManagerMenu())

            PrimaryRoles.SALESMAN in roles -> add(SalesmanMenu())
        }
    }
}