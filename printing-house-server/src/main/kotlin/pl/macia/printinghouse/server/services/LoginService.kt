package pl.macia.printinghouse.server.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import pl.macia.printinghouse.request.LoginReq
import pl.macia.printinghouse.response.BasicLoginResp
import pl.macia.printinghouse.roles.PrimaryRoles

@Service
class LoginService {
    @Autowired
    private lateinit var authenticationManager: AuthenticationManager
    fun getEmplData(loginReq: LoginReq): BasicLoginResp {
        val authentication =
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(loginReq.login, loginReq.password))
        val roles = authentication.authorities.map {
            it.authority
        }.filter {
            it in listOf(
                PrimaryRoles.ADMIN,
                PrimaryRoles.WORKER,
                PrimaryRoles.MANAGER,
                PrimaryRoles.EMPLOYEE,
                PrimaryRoles.SALESMAN,
                PrimaryRoles.WORKFLOW_STAGE_MANAGER,
                PrimaryRoles.OWNER
            )
        }
        return BasicLoginResp(authentication.isAuthenticated, roles)
    }
}