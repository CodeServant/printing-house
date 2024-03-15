package pl.macia.printinghouse.web.cli

import io.kvision.html.Button
import io.kvision.panel.SimplePanel
import pl.macia.printinghouse.request.OrderReq

class SalesmanNewOrderPanel(
    onSave: (OrderReq) -> Unit,
    onAccept: (OrderReq) -> Unit,
    onLeave: () -> Unit,
    init: (SalesmanNewOrderPanel.() -> Unit)? = null
) :
    SimplePanel() {
    init {
        val acceptBtn = AcceptButton() {
            onClick {
                onAccept(TODO())
            }
        }
        val saveBtn = SaveButton() {
            onClick {
                onSave(TODO())
            }
        }
        val leaveBtn = CancelButton() {
            onClick {
                onLeave()
            }
        }
        val inOrdPanel = InsertOrderPanel()
        add(inOrdPanel)
        add(acceptBtn)
        add(saveBtn)
        add(leaveBtn)
        init?.invoke(this)
    }
}

private class AcceptButton(init: (AcceptButton.() -> Unit)? = null) : Button("accept") {
    init {
        init?.invoke(this)
    }
}

private class SaveButton(init: (SaveButton.() -> Unit)? = null) : Button("save") {
    init {
        init?.invoke(this)
    }
}