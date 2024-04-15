package pl.macia.printinghouse.web.dao

import io.kvision.rest.RestResponse
import pl.macia.printinghouse.request.BindingFormChangeReq
import pl.macia.printinghouse.request.BindingFormReq
import pl.macia.printinghouse.response.BindingFormResp
import pl.macia.printinghouse.response.ChangeResp
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.web.clientConfig

class BindingFormDao {
    private val url = "${clientConfig.serviceUrl}/api/binding-forms"
    private val dullDao = DullDao(url)
    fun allBindingForms(onFulfilled: (List<BindingFormResp>) -> Unit, onRejected: (Throwable) -> Unit) =
        dullDao.getAllDull(onFulfilled, onRejected)

    fun getBindingForm(
        id: Int,
        onFulfilled: (BindingFormResp) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.getOneDull(id, onFulfilled, onRejected)

    fun insertBindingForm(
        dullObjReq: BindingFormReq,
        onFulfilled: (RecID) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.newDullObj(dullObjReq, onFulfilled, onRejected)

    fun delBindingForm(
        id: Int,
        onFulfilled: (RestResponse<dynamic>) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.delDullObj(id, onFulfilled, onRejected)

    fun changeBindingForm(
        id: Int,
        changeReq: BindingFormChangeReq,
        onFulfilled: (ChangeResp) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.changeDullObj(id, changeReq, onFulfilled, onRejected)
}