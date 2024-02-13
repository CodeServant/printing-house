package pl.macia.printinghouse.web.dao

import pl.macia.printinghouse.request.ClientChangeReq
import pl.macia.printinghouse.request.ClientReq
import pl.macia.printinghouse.response.ChangeResp
import pl.macia.printinghouse.response.ClientResp
import pl.macia.printinghouse.response.RecID

class ClientDao {
    private val url = "http://localhost:8080/api/clients"
    private val dullDao = DullDao(url)
    fun newClientReq(
        clientReq: ClientReq,
        onFulfilled: (RecID) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.newDullObj(clientReq, onFulfilled, onRejected)

    fun getClient(
        id: Int,
        onFulfilled: (ClientResp) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.getOneDull(id, onFulfilled, onRejected)

    fun changeClient(
        id: Int,
        changeReq: ClientChangeReq,
        onFulfilled: (ChangeResp) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.changeDullObj(
        id,
        changeReq,
        onFulfilled,
        onRejected
    )
}