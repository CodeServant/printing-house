package pl.macia.printinghouse.server.controller

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.web.bind.annotation.*
import pl.macia.printinghouse.request.ClientChangeReq
import pl.macia.printinghouse.request.ClientReq
import pl.macia.printinghouse.response.ChangeResp
import pl.macia.printinghouse.response.ClientResp
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.services.ClientService
import java.util.*

@RestController
@RequestMapping(EndpNames.API_CONTEXT)
@EnableMethodSecurity(prePostEnabled = true)
@Tag(name = "client controller", description = "helps to add, delete and change data of clients")
@CrossOrigin
class ClientController {
    @Autowired
    private lateinit var serv: ClientService

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}','${PrimaryRoles.SALESMAN}')")
    @GetMapping(value = ["${EndpNames.Clients.CLIENTS}/{id}"], produces = ["application/json"])
    fun getClientById(@PathVariable id: Int): ResponseEntity<ClientResp> {
        val cli = Optional.ofNullable(serv.findById(id))
        return ResponseEntity.of(cli)
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}','${PrimaryRoles.SALESMAN}')")
    @PostMapping(value = [EndpNames.Clients.CLIENTS], produces = ["application/json"])
    fun newClient(@RequestBody newClient: ClientReq): ResponseEntity<RecID> {
        val id = serv.createNew(newClient)
        return ResponseEntity.ok(id)
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}','${PrimaryRoles.SALESMAN}')")
    @PutMapping(value = ["${EndpNames.Clients.CLIENTS}/{id}"], produces = ["application/json"])
    fun changeClient(
        @PathVariable id: Int,
        @RequestBody clientChange: ClientChangeReq
    ): ResponseEntity<ChangeResp> {
        val changed = serv.changeOne(id, clientChange)
        return ResponseEntity.of(Optional.ofNullable(changed))
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}','${PrimaryRoles.SALESMAN}')")
    @GetMapping(value = [EndpNames.Clients.CLIENTS], produces = ["application/json"])
    fun getClients(
        @Parameter(
            required = false,
            description = "filter clients using searching query"
        )
        @RequestParam
        searchQuery: String
    ): ResponseEntity<Collection<ClientResp>> {
        return ResponseEntity.ok(serv.queryClient(searchQuery))
    }
}