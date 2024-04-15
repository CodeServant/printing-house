package pl.macia.printinghouse.web.dao

import io.kvision.rest.RestClient
import io.kvision.rest.call
import io.kvision.utils.obj
import pl.macia.printinghouse.response.OrderResp
import pl.macia.printinghouse.web.authorize
import pl.macia.printinghouse.web.clientConfig

class OrderDao {
    private val url = "${clientConfig.serviceUrl}/api/orders"
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