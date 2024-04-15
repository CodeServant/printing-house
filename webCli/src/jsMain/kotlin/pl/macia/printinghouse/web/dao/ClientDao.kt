package pl.macia.printinghouse.web.dao

import io.kvision.rest.RestClient
import io.kvision.rest.call
import io.kvision.utils.obj
import pl.macia.printinghouse.request.ClientChangeReq
import pl.macia.printinghouse.request.ClientReq
import pl.macia.printinghouse.response.ChangeResp
import pl.macia.printinghouse.response.ClientResp
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.web.authorize
import pl.macia.printinghouse.web.clientConfig

class ClientDao {
    private val url = "${clientConfig.serviceUrl}/api/clients"
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

    fun searchClient(
        query: String,
        onFulfilled: (List<ClientResp>) -> Unit,
        onRejected: (Throwable) -> Unit
    ) {
        val restClient = RestClient()
        val premise = restClient.call<List<ClientResp>>(
            url,
        ) {
            data = obj {
                searchQuery = query
            }
            authorize()
        }
        premise.then(onFulfilled, onRejected)
    }
}