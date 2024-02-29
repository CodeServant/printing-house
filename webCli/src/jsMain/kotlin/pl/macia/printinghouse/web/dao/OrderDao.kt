package pl.macia.printinghouse.web.dao

import io.kvision.rest.RestClient
import io.kvision.rest.call
import io.kvision.utils.obj
import pl.macia.printinghouse.response.OrderResp
import pl.macia.printinghouse.web.authorize

class OrderDao {
    private val url = "http://localhost:8080/api/orders"
    fun getAssigneeseOrders(onFulfilled: (List<OrderResp>) -> Unit, onRejected: (Throwable) -> Unit) {
        val restClient = RestClient()
        val premise = restClient.call<List<OrderResp>>(url) {
            authorize()
            data = obj {
                currentWorker = true
            }
        }
        premise.then(onFulfilled, onRejected)
    }
}