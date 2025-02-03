package pl.macia.printinghouse.web

import kotlinx.serialization.json.Json
import org.w3c.dom.Storage
import org.w3c.dom.get

/**
 * This is helper class to access storage like [kotlinx.browser.localStorage], to save user preferences.
 */
class StorageInfo(private val storage: Storage) {
    var username: String?
        get() {
            return storage["user_name"]
        }
        set(value) {
            if (value != null)
                storage.setItem("user_name", value)
            else
                storage.removeItem("user_name")
        }
    var token: String?
        get() {
            return storage["user_access_token"]
        }
        set(value) {
            if (value != null)
                storage.setItem("user_access_token", value)
            else
                storage.removeItem("user_access_token")
        }
    var userRoles: List<String>?
        get() {
            val rolesString = storage["user_roles"]
            return if (rolesString != null)
                Json.decodeFromString<List<String>>(rolesString)
            else
                null
        }
        set(value) {
            if (value != null)
                storage.setItem("user_roles", Json.encodeToString(value))
            else
                storage.removeItem("user_roles")
        }
}

/**
 * Checks if user is logged.
 */
fun StorageInfo.logged(): Boolean {
    return username != null
}

/**
 * Log out the current user.
 */
fun StorageInfo.logout() {
    username = null
    token = null
    userRoles = null
}