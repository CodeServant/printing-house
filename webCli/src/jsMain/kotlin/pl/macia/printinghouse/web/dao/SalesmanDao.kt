package pl.macia.printinghouse.web.dao

import io.kvision.rest.RestResponse
import pl.macia.printinghouse.request.SalesmanChangeReq
import pl.macia.printinghouse.request.SalesmanReq
import pl.macia.printinghouse.request.WorkerChangeReq
import pl.macia.printinghouse.request.WorkerReq
import pl.macia.printinghouse.response.ChangeResp
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.response.SalesmanResp
import pl.macia.printinghouse.response.WorkerResp

class SalesmanDao {
    private val url = "http://localhost:8080/api/salesmans"
    private val dullDao = DullDao(url)
    fun allSalesmans(onFulfilled: (List<SalesmanResp>) -> Unit, onRejected: (Throwable) -> Unit) =
        dullDao.getAllDull(onFulfilled, onRejected)

    fun newSalesmanReq(
        salesmanReq: SalesmanReq,
        onFulfilled: (RecID) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.newDullObj(salesmanReq, onFulfilled, onRejected)

    fun getSalesman(
        id: Int,
        onFulfilled: (SalesmanResp) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.getOneDull(id, onFulfilled, onRejected)

    fun delSalesman(
        id: Int,
        onFulfilled: (RestResponse<dynamic>) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.delDullObj(id, onFulfilled, onRejected)

    fun changeSalesman(
        id: Int,
        changeReq: SalesmanChangeReq,
        onFulfilled: (ChangeResp) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.changeDullObj(
        id,
        changeReq,
        onFulfilled,
        onRejected
    )
}