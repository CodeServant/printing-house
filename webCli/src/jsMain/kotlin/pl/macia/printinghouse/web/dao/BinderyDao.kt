package pl.macia.printinghouse.web.dao

import io.kvision.rest.*
import pl.macia.printinghouse.request.BinderyChangeReq
import pl.macia.printinghouse.request.BinderyReq
import pl.macia.printinghouse.response.BinderyResp
import pl.macia.printinghouse.response.ChangeResp
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.web.authorize

class BinderyDao {
    private val url = "http://localhost:8080/api/binderies"
    private val dullDao = DullDao(url)
    fun allBinderies(onFulfilled: (List<BinderyResp>) -> Unit, onRejected: (Throwable) -> Unit) =
        dullDao.getAllDull(onFulfilled, onRejected)

    fun newBinderyReq(
        binderyReq: BinderyReq,
        onFulfilled: (RecID) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.newDullObj(binderyReq, onFulfilled, onRejected)

    fun getBindery(
        id: Int,
        onFulfilled: (BinderyResp) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.getOneDull<BinderyResp>(id, onFulfilled, onRejected)

    fun delBindery(id: Int, onFulfilled: (RestResponse<dynamic>) -> Unit, onRejected: (Throwable) -> Unit) {
        val restClient = RestClient()
        val premise = restClient.requestDynamic(
            url = "$url/$id"
        ) {
            method = HttpMethod.DELETE
            authorize()
        }
        premise.then(onFulfilled, onRejected)
    }

    fun changeBindery(
        id: Int,
        changeReq: BinderyChangeReq,
        onFulfilled: (ChangeResp) -> Unit,
        onRejected: (Throwable) -> Unit
    ) {
        val restClient = RestClient()
        val premise = restClient.call<ChangeResp, BinderyChangeReq>(
            url = "$url/$id",
            data = changeReq
        ) {
            method = HttpMethod.PUT
            authorize()
        }
        premise.then(onFulfilled, onRejected)
    }
}