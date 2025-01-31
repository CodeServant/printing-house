package pl.macia.printinghouse.server.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.macia.printinghouse.request.LoginReq
import pl.macia.printinghouse.response.BaererLoginResp
import pl.macia.printinghouse.server.services.LoginService

@RestController
@RequestMapping(EndpNames.API_CONTEXT)
@EnableMethodSecurity(prePostEnabled = true)
@Tag(name = "LoginController", description = "this endpoint enable user to authenticate and thus login to service")
@CrossOrigin
class LoginController {
    @Autowired
    private lateinit var serv: LoginService

    @PostMapping(value = [EndpNames.Login.LOGIN], produces = ["application/json"])
    fun login(@RequestBody loginReq: LoginReq): ResponseEntity<BaererLoginResp> {
        val authResp = serv.getEmplData(loginReq)
        if (!authResp.authenticated) {
            return ResponseEntity.status(401).build()
        }
        return ResponseEntity.ofNullable(authResp)
    }
}