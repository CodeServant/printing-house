package pl.macia.printinghouse.web.dao

import io.kvision.rest.HttpMethod
import io.kvision.rest.RestClient
import io.kvision.rest.request
import kotlinx.browser.localStorage
import pl.macia.printinghouse.request.LoginReq
import pl.macia.printinghouse.response.BaererLoginResp
import pl.macia.printinghouse.web.StorageInfo
import pl.macia.printinghouse.web.clientConfig

class LoginDao {
    /**
     * Login fetching identity of a user to [localStorage].
     * @param login identity of a user (for employee it is email)
     * @param onException what to do when exception happens while signing in
     */
    fun signIn(login: String, password: String, onSuccess: () -> Unit, onException: (Throwable) -> Unit) {
        val restClient = RestClient()
        val premise = restClient.request<BaererLoginResp, LoginReq>(
            "${clientConfig.serviceUrl}/api/signin",
            LoginReq(login, password)
        ) {
            method = HttpMethod.POST
            contentType = "application/json"
        }
        premise.then({
            if (it.data.authenticated) {
                val storage = StorageInfo(localStorage)
                storage.username = login
                storage.token = it.data.token
                storage.userRoles = it.data.roles
                onSuccess()
            }
        }, onException)
    }
}