package pl.macia.printinghouse.web.dao

import io.kvision.rest.RestClient
import io.kvision.rest.call
import pl.macia.printinghouse.web.authorize

/**
 * This is util dao for other dao to reduce repetitive tasks for writing simple CRUD dao.
 */
class DullDao(val url: String) {
    internal inline fun <reified DullResp> getAllDull(
        noinline onFulfilled: (List<DullResp>) -> Unit,
        noinline onRejected: (Throwable) -> Unit
    ) {
        val restClient = RestClient()
        val premise = restClient.call<List<DullResp>>(
            url = url,
        ) {
            authorize()
        }
        premise.then(onFulfilled, onRejected)
    }
}