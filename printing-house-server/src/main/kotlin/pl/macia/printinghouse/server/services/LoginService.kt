package pl.macia.printinghouse.server.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import pl.macia.printinghouse.request.LoginReq
import pl.macia.printinghouse.response.BaererLoginResp
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.config.JwtService

@Service
class LoginService {
    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var jwtServ: JwtService

    fun getEmplData(loginReq: LoginReq): BaererLoginResp {
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
        val token = jwtServ.generateToken(loginReq.login)
        return BaererLoginResp(authentication.isAuthenticated, token, roles)
    }
}