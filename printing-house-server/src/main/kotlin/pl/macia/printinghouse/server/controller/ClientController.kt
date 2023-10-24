package pl.macia.printinghouse.server.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.macia.printinghouse.response.ClientResp
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.services.ClientService
import java.util.Optional

@RestController
@RequestMapping(EndpNames.API_CONTEXT)
@EnableMethodSecurity(prePostEnabled = true)
@Tag(name = "client controller", description = "helps to add, delete and change data of clients")
class ClientController {
    @Autowired
    private lateinit var serv: ClientService

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}','${PrimaryRoles.SALESMAN}')")
    @GetMapping(value = ["${EndpNames.Clients.CLIENTS}/{id}"], produces = ["application/json"])
    fun getClientById(@PathVariable id: Int): ResponseEntity<ClientResp> {
        val cli = Optional.ofNullable(serv.findById(id))
        return ResponseEntity.of(cli)
    }
}