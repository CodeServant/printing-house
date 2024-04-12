package pl.macia.printinghouse.web.dao

import pl.macia.printinghouse.response.ImpositionTypeResp

class ImpositionTypeDao {
    private val url = "http://localhost:8080/api/imposition-types"
    private val dullDao = DullDao(url)
    fun allImpositionTypes(onFulfilled: (List<ImpositionTypeResp>) -> Unit, onRejected: (Throwable) -> Unit) =
        dullDao.getAllDull(onFulfilled, onRejected)
}