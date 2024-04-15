package pl.macia.printinghouse.web.dao

import pl.macia.printinghouse.response.SizeResp
import pl.macia.printinghouse.web.clientConfig

class SizeDao {
    private val url = "${clientConfig.serviceUrl}/api/sizes"
    private val dullDao = DullDao(url)
    fun allNamedSizes(onFulfilled: (List<SizeResp>) -> Unit, onRejected: (Throwable) -> Unit) =
        dullDao.getAllDull(onFulfilled, onRejected)
}