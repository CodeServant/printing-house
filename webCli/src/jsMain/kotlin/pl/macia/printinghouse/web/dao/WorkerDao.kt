package pl.macia.printinghouse.web.dao

import io.kvision.rest.RestResponse
import pl.macia.printinghouse.request.BinderyChangeReq
import pl.macia.printinghouse.request.BinderyReq
import pl.macia.printinghouse.request.WorkerChangeReq
import pl.macia.printinghouse.request.WorkerReq
import pl.macia.printinghouse.response.ChangeResp
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.response.WorkerResp

class WorkerDao {
    private val url = "http://localhost:8080/api/workers"
    private val dullDao = DullDao(url)
    fun allWorkers(onFulfilled: (List<WorkerResp>) -> Unit, onRejected: (Throwable) -> Unit) =
        dullDao.getAllDull(onFulfilled, onRejected)

    fun newWorkerReq(
        workerReq: WorkerReq,
        onFulfilled: (RecID) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.newDullObj(workerReq, onFulfilled, onRejected)

    fun getWorker(
        id: Int,
        onFulfilled: (WorkerResp) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.getOneDull(id, onFulfilled, onRejected)

    fun delWorker(
        id: Int,
        onFulfilled: (RestResponse<dynamic>) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.delDullObj(id, onFulfilled, onRejected)

    fun changeWorker(
        id: Int,
        changeReq: WorkerChangeReq,
        onFulfilled: (ChangeResp) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.changeDullObj(
        id,
        changeReq,
        onFulfilled,
        onRejected
    )
}