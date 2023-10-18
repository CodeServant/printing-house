package pl.macia.printinghouse.server.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.macia.printinghouse.response.BinderyResp
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.services.BinderyService

@RestController
@RequestMapping(EndpNames.API_CONTEXT)
@EnableMethodSecurity(prePostEnabled = true)
@Tag(name = "bindery", description = "binderies controller")
class BinderyController {
    @Autowired
    private lateinit var serv: BinderyService

    @Operation(summary = "get all binderies")
    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @GetMapping(value = [EndpNames.Bindery.BINDERIES], produces = ["application/json"])
    fun getAllBinderies(): ResponseEntity<List<BinderyResp>> {
        return ResponseEntity.ok(serv.listAllBinderies())
    }
}