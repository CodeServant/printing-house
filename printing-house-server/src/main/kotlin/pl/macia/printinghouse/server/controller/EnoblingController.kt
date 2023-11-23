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
import pl.macia.printinghouse.response.EnoblingResp
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.services.EnoblingService
import java.util.*

@RestController
@RequestMapping(EndpNames.API_CONTEXT)
@EnableMethodSecurity(prePostEnabled = true)
@Tag(name = "Enobling", description = "controller for managing general enoblings")
class EnoblingController {
    @Autowired
    private lateinit var serv: EnoblingService

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.EMPLOYEE}')")
    @GetMapping(value = ["${EndpNames.Enobling.ENOBLINGS}/{id}"], produces = ["application/json"])
    fun findById(@PathVariable id: Int): ResponseEntity<EnoblingResp> {
        val found = Optional.ofNullable(serv.findById(id))
        return ResponseEntity.of(found)
    }
}