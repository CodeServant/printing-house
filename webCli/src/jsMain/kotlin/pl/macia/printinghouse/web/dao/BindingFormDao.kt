package pl.macia.printinghouse.web.dao

import pl.macia.printinghouse.response.BindingFormResp

class BindingFormDao {
    private val url = "http://localhost:8080/api/binding-forms"
    private val dullDao = DullDao(url)
    fun allBindingForms(onFulfilled: (List<BindingFormResp>) -> Unit, onRejected: (Throwable) -> Unit) = dullDao.getAllDull(onFulfilled, onRejected)

}