package pl.macia.printinghouse.web.dao

import pl.macia.printinghouse.response.ImpositionTypeResp
import pl.macia.printinghouse.web.clientConfig

class ImpositionTypeDao {
    private val url = "${clientConfig.serviceUrl}/api/imposition-types"
    private val dullDao = DullDao(url)
    fun allImpositionTypes(onFulfilled: (List<ImpositionTypeResp>) -> Unit, onRejected: (Throwable) -> Unit) =
        dullDao.getAllDull(onFulfilled, onRejected)
}