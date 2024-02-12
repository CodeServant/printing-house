package pl.macia.printinghouse.web.dao

import io.kvision.rest.*
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

    internal fun delDullObj(id: Int, onFulfilled: (RestResponse<dynamic>) -> Unit, onRejected: (Throwable) -> Unit) {
        val restClient = RestClient()
        val premise = restClient.requestDynamic(
            url = "$url/$id"
        ) {
            method = HttpMethod.DELETE
            authorize()
        }
        premise.then(onFulfilled, onRejected)
    }

    internal inline fun <reified DullChangeReq : Any, reified ChangeResp : Any> changeDullObj(
        id: Int,
        changeReq: DullChangeReq,
        noinline onFulfilled: (ChangeResp) -> Unit,
        noinline onRejected: (Throwable) -> Unit
    ) {
        val restClient = RestClient()
        val premise = restClient.call<ChangeResp, DullChangeReq>(
            url = "$url/$id",
            data = changeReq
        ) {
            method = HttpMethod.PUT
            authorize()
        }
        premise.then(onFulfilled, onRejected)
    }
}