package pl.macia.printinghouse.web.dao

import io.kvision.rest.HttpMethod
import io.kvision.rest.RestClient
import io.kvision.rest.call
import pl.macia.printinghouse.web.authorize

/**
 * This is util dao for other dao to reduce repetitive tasks for writing simple CRUD dao.
 */
class DullDao(val url: String) {
    internal inline fun <reified DullResp> getAllDull(
        noinline onFulfilled: (List<DullResp>) -> Unit,
        noinline onRejected: (Throwable) -> Unit
    ) {
        val restClient = RestClient()
        val premise = restClient.call<List<DullResp>>(
            url = url,
        ) {
            authorize()
        }
        premise.then(onFulfilled, onRejected)
    }

    internal inline fun <reified DullResp : Any> getOneDull(
        id: Int,
        noinline onFulfilled: (DullResp) -> Unit,
        noinline onRejected: (Throwable) -> Unit
    ) {
        val restClient = RestClient()
        val premise = restClient.call<DullResp>(
            url = "$url/$id"
        ) {
            authorize()
        }
        premise.then(onFulfilled, onRejected)
    }

    internal inline fun <reified IDResp : Any, reified DullObjReq : Any> newDullObj(
        dullObjReq: DullObjReq,
        noinline onFulfilled: (IDResp) -> Unit,
        noinline onRejected: (Throwable) -> Unit
    ) {
        val restClient = RestClient()
        val premise = restClient.call<IDResp, DullObjReq>(
            url, data = dullObjReq
        ) {
            method = HttpMethod.POST
            authorize()
        }
        premise.then(onFulfilled, onRejected)
    }
}