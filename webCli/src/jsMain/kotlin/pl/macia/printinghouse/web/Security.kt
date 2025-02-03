package pl.macia.printinghouse.web

import io.kvision.require
import io.kvision.rest.RestRequestConfig
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.datetime.Clock.System
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

private const val offset = 5000L

/**
 * Use [localStorage] to authorize rest request.
 */
internal fun RestRequestConfig<dynamic, dynamic>.authorize() {
    val jwtDecose = require("jwt-decode")
    val storage = StorageInfo(localStorage)
    val exp = jwtDecose.jwtDecode(storage.token.toString()).exp.toString().toLong()
    val expDate = Instant.fromEpochSeconds(exp - offset).changeToDayStart()
    val expired = expDate.compareTo(System.now()) < 0
    if (!expired) {
        headers = {
            listOf(
                Pair("Authorization", "Bearer ${storage.token}")
            )
        }
    } else {
        logout(storage)
    }
}

/**
 * This function shifts [Instant] time to start of the day to prevent user logout while work.
 */
private fun Instant.changeToDayStart(): Instant {
    return Instant.fromEpochMilliseconds(this.toLocalDateTime(TimeZone.currentSystemDefault()).date.toEpochDays() * 24L * 60L * 60L * 1000L)
}

private fun logout(storage: StorageInfo) {
    storage.logout()
    window.location.reload()
}