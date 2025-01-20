package pl.macia.printinghouse.web.cli

import io.kvision.html.Button
import io.kvision.panel.SimplePanel
import pl.macia.printinghouse.request.OrderReq
import pl.macia.printinghouse.web.dao.OrderDao

class SalesmanNewOrderPanel(
    onSave: (OrderReq) -> Unit,
    onAccept: (OrderReq) -> Unit,
    onLeave: () -> Unit, // todo make on error function when fetching data is disrupted
    init: (SalesmanNewOrderPanel.() -> Unit)? = null
) : SimplePanel() {

    init {
        val inOrdPanel = InsertOrderPanel()
        add(inOrdPanel)
        val acceptBtn = AcceptButton {
            onClick {
                onAccept(TODO())
            }
        }
        val saveBtn = SaveButton {
            onClick {
                val data = inOrdPanel.getFormData(true)
                if (data != null) {
                    OrderDao().insertNew(data.toTransport(), onFulfilled = {
                        insertToast("order saved succesfully")
                        onLeave()
                    }, onRejected = {
                        failToast("order insert failed", "insertion failed")
                    })
                }
            }
        }
        val leaveBtn = CancelButton {
            onClick {
                onLeave()
            }
        }
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

private fun InsertOrderPanel.toOrderReq(): OrderReq {
    TODO("")
}