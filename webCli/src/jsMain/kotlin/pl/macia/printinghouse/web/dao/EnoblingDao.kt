package pl.macia.printinghouse.web.dao

import pl.macia.printinghouse.request.IEnoblingChangeReq
import pl.macia.printinghouse.request.IEnoblingReq
import pl.macia.printinghouse.response.ChangeResp
import pl.macia.printinghouse.response.EnoblingResp
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.web.clientConfig

class EnoblingDao {
    private val url = "${clientConfig.serviceUrl}/api/enoblings"
    private val dullDao = DullDao(url)
    fun allEnoblings(
        onFulfilled: (List<EnoblingResp>) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.getAllDull(onFulfilled, onRejected)

    fun newEnoblingReq(
        enoblingReq: IEnoblingReq,
        onFulfilled: (RecID) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.newDullObj(enoblingReq, onFulfilled, onRejected)

    fun getEnobling(
        id: Int,
        onFulfilled: (EnoblingResp) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.getOneDull(id, onFulfilled, onRejected)

    fun changeEnobling(
        id: Int,
        changeReq: IEnoblingChangeReq,
        onFulfilled: (ChangeResp) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.changeDullObj(
        id,
        changeReq,
        onFulfilled,
        onRejected
    )
}