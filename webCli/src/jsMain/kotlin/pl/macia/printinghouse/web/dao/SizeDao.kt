package pl.macia.printinghouse.web.dao

import pl.macia.printinghouse.response.SizeResp

class SizeDao {
    private val url = "http://localhost:8080/api/sizes"
    private val dullDao = DullDao(url)
    fun allNamedSizes(onFulfilled: (List<SizeResp>) -> Unit, onRejected: (Throwable) -> Unit) =
        dullDao.getAllDull(onFulfilled, onRejected)
}