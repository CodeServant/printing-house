package pl.macia.printinghouse.web.cli

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
        add(InsertOrderPanel())
        init?.invoke(this)
    }
}