package pl.macia.printinghouse.web

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.dom.Storage
import org.w3c.dom.get
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

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
    var userpass: String?
        get() {
            return storage["user_password"]
        }
        set(value) {
            if (value != null)
                storage.setItem("user_password", value)
            else
                storage.removeItem("user_password")
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

@OptIn(ExperimentalEncodingApi::class)
fun StorageInfo.basicAuthToken(): String {
    val message = "$username:$userpass"
    return Base64.encode(message.encodeToByteArray())
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
    userpass = null
    userRoles = null
}