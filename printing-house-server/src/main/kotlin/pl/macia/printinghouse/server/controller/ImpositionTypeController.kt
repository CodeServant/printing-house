package pl.macia.printinghouse.server.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.web.bind.annotation.*
import pl.macia.printinghouse.request.ImpositionTypeChangeReq
import pl.macia.printinghouse.request.ImpositionTypeReq
import pl.macia.printinghouse.response.ChangeResp
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.services.ImpositionTypeService
import pl.macia.printinghouse.response.ImpositionTypeResp
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.server.services.changeExceptionCatch
import java.util.*

@RestController
@RequestMapping(EndpNames.API_CONTEXT)
@EnableMethodSecurity(prePostEnabled = true)
@Tag(name = "Imposition Type", description = "controller for managing imposition types")
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

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @PostMapping(value = [EndpNames.ImpositionType.IMPOSITION_TYPES], produces = ["application/json"])
    fun newImpositionType(@RequestBody req: ImpositionTypeReq): ResponseEntity<RecID> {
        val resp = serv.insertNew(req)
        return ResponseEntity.ok(resp)
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @PutMapping(value = ["${EndpNames.ImpositionType.IMPOSITION_TYPES}/{id}"], produces = ["application/json"])
    fun changeImpositionType(
        @PathVariable id: Int,
        @RequestBody req: ImpositionTypeChangeReq
    ): ResponseEntity<ChangeResp> {
        return changeExceptionCatch {
            val changeResp: Optional<ChangeResp> =
                Optional.ofNullable(serv.changeImpositionType(id = id, changeReq = req))
            ResponseEntity.of(changeResp)
        }
    }
}