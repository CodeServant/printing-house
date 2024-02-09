package pl.macia.printinghouse.web.dao

import io.kvision.rest.*
import kotlinx.browser.localStorage
import pl.macia.printinghouse.request.BinderyReq
import pl.macia.printinghouse.response.BinderyResp
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.web.StorageInfo
import pl.macia.printinghouse.web.basicAuthToken

class BinderyDao {
    private val url = "http://localhost:8080/api/binderies"
    private fun RestRequestConfig<dynamic, dynamic>.authorize() {
        val storage = StorageInfo(localStorage)
        headers = {
            listOf(
                Pair("Authorization", "Basic ${storage.basicAuthToken()}")
            )
        }
    }

    fun allBinderies(onFulfilled: (List<BinderyResp>) -> Unit, onRejected: (Throwable) -> Unit) {
        val restClient = RestClient()
        val premise = restClient.call<List<BinderyResp>>(
            url = url,
        ) {
            authorize()
        }
        premise.then(onFulfilled, onRejected)
    }

    fun newBinderyReq(
        binderyReq: BinderyReq,
        onFulfilled: (RecID) -> Unit,
        onRejected: (Throwable) -> Unit
    ) {
        val restClient = RestClient()
        val premise = restClient.call<RecID, BinderyReq>(
            url, data = binderyReq
        ) {
            method = HttpMethod.POST
            authorize()
        }
        premise.then(onFulfilled, onRejected)
    }

    fun getBindery(
        id: Int,
        onFulfilled: (BinderyResp) -> Unit,
        onRejected: (Throwable) -> Unit
    ) {
        val restClient = RestClient()
        val premise = restClient.call<BinderyResp>(
            url = "$url/$id"
        ) {
            authorize()
        }
        premise.then(onFulfilled, onRejected)
    }
}