package pl.macia.printinghouse.web

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.dom.Storage
import org.w3c.dom.get

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