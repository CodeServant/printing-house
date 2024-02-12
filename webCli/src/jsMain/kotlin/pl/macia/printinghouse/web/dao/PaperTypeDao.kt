package pl.macia.printinghouse.web.dao

import pl.macia.printinghouse.request.PaperTypeChangeReq
import pl.macia.printinghouse.request.PaperTypeReq
import pl.macia.printinghouse.response.ChangeResp
import pl.macia.printinghouse.response.PaperTypeResp
import pl.macia.printinghouse.response.RecID

class PaperTypeDao {
    private val url = "http://localhost:8080/api/paper-types"
    private val dullDao = DullDao(url)
    fun allPaperTypes(onFulfilled: (List<PaperTypeResp>) -> Unit, onRejected: (Throwable) -> Unit) =
        dullDao.getAllDull(onFulfilled, onRejected)

    fun newPaperTypeReq(
        paperTypeReq: PaperTypeReq,
        onFulfilled: (RecID) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.newDullObj(paperTypeReq, onFulfilled, onRejected)

    fun getPaperType(
        id: Int,
        onFulfilled: (PaperTypeResp) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.getOneDull(id, onFulfilled, onRejected)

    fun changePaperType(
        id: Int,
        changeReq: PaperTypeChangeReq,
        onFulfilled: (ChangeResp) -> Unit,
        onRejected: (Throwable) -> Unit
    ) = dullDao.changeDullObj(
        id,
        changeReq,
        onFulfilled,
        onRejected
    )
}