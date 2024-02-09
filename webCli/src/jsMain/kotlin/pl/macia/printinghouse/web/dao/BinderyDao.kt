package pl.macia.printinghouse.web.dao

import io.kvision.rest.RestClient
import io.kvision.rest.call
import kotlinx.browser.localStorage
import pl.macia.printinghouse.response.BinderyResp
import pl.macia.printinghouse.web.StorageInfo
import pl.macia.printinghouse.web.basicAuthToken

class BinderyDao {
    fun allBinderies(onFulfilled: (List<BinderyResp>) -> Unit, onRejected: (Throwable) -> Unit) {
        val restClient = RestClient()
        val premise = restClient.call<List<BinderyResp>>(
            url = "http://localhost:8080/api/binderies",
        ){
            val storage = StorageInfo(localStorage)
            headers = {
                listOf(
                    Pair("Authorizatsion","Basic ${storage.basicAuthToken()}")
                )
            }
        }
        premise.then(onFulfilled, onRejected)
    }
}