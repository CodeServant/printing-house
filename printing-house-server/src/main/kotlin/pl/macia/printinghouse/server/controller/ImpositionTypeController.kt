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
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.services.ImpositionTypeService
import pl.macia.printinghouse.response.ImpositionTypeResp
import java.util.*

@RestController
@RequestMapping(EndpNames.API_CONTEXT)
@EnableMethodSecurity(prePostEnabled = true)
@Tag(name = "printer", description = "controller for managing imposition types")
class ImpositionTypeController {
    @Autowired
    private lateinit var serv: ImpositionTypeService

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @GetMapping(value = ["${EndpNames.ImpositionType.IMPOSITION_TYPES}/{id}"], produces = ["application/json"])
    fun findById(@PathVariable id: Int): ResponseEntity<ImpositionTypeResp> {
        val found = Optional.ofNullable(serv.findById(id))
        return ResponseEntity.of(found)
    }
    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @GetMapping(value = [EndpNames.ImpositionType.IMPOSITION_TYPES], produces = ["application/json"])
    fun getAllImpositionTypes(): ResponseEntity<List<ImpositionTypeResp>> {
        return ResponseEntity.ok(serv.allImpositionTypes())
    }
}