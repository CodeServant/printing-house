package pl.macia.printinghouse.web

import io.kvision.rest.RestRequestConfig
import kotlinx.browser.localStorage

/**
 * Use [localStorage] to authorize rest request.
 */
internal fun RestRequestConfig<dynamic, dynamic>.authorize() {
    val storage = StorageInfo(localStorage)
    headers = {
        listOf(
            Pair("Authorization", "Bearer ${storage.token}")
        )
    }
}