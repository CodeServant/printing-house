package pl.macia.printinghouse.web.cli

import io.kvision.html.Button
import io.kvision.panel.SimplePanel
import io.kvision.state.ObservableValue
import pl.macia.printinghouse.request.OrderReq
import pl.macia.printinghouse.response.SizeResp
import pl.macia.printinghouse.web.dao.SizeDao

class SalesmanNewOrderPanel(
    onSave: (OrderReq) -> Unit,
    onAccept: (OrderReq) -> Unit,
    onLeave: () -> Unit, // todo make on error function when fetching data is disrupted
    init: (SalesmanNewOrderPanel.() -> Unit)? = null
) :
    SimplePanel() {
    private val loaded = ObservableValue<List<SizeResp>?>(null)

    init {
        SizeDao().allNamedSizes(
            onFulfilled = {
                loaded.value = it
            },
            onRejected = {}
        )

        loaded.subscribe {
            if (it != null) {
                val inOrdPanel = InsertOrderPanel(it)
                add(inOrdPanel)
                val acceptBtn = AcceptButton() {
                    onClick {
                        onAccept(TODO())
                    }
                }
                val saveBtn = SaveButton() {
                    onClick {
                        inOrdPanel.getFormData(true)
                    }
                }
                val leaveBtn = CancelButton() {
                    onClick {
                        onLeave()
                    }
                }
                add(acceptBtn)
                add(saveBtn)
                add(leaveBtn)
            }
        }

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