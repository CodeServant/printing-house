package pl.macia.printinghouse.server.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.web.bind.annotation.*
import pl.macia.printinghouse.request.BinderyChangeReq
import pl.macia.printinghouse.request.BinderyReq
import pl.macia.printinghouse.response.BinderyResp
import pl.macia.printinghouse.response.ChangeResp
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.services.BinderyService
import java.util.*

@RestController
@RequestMapping(EndpNames.API_CONTEXT)
@EnableMethodSecurity(prePostEnabled = true)
@Tag(name = "bindery", description = "binderies controller")
@CrossOrigin
class BinderyController {
    @Autowired
    private lateinit var serv: BinderyService

    @Operation(summary = "get all binderies")
    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}','${PrimaryRoles.SALESMAN}')")
    @GetMapping(value = [EndpNames.Bindery.BINDERIES], produces = ["application/json"])
    fun getAllBinderies(): ResponseEntity<List<BinderyResp>> {
        return ResponseEntity.ok(serv.listAllBinderies())
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}','${PrimaryRoles.SALESMAN}')")
    @GetMapping(value = ["${EndpNames.Bindery.BINDERIES}/{id}"], produces = ["application/json"])
    fun findById(@PathVariable id: Int): ResponseEntity<BinderyResp> {
        val found = Optional.ofNullable(serv.findById(id))
        return ResponseEntity.of(found)
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @PostMapping(value = [EndpNames.Bindery.BINDERIES], produces = ["application/json"])
    fun newBindery(@RequestBody req: BinderyReq): ResponseEntity<RecID> {
        val resp = serv.insertNew(req)
        return ResponseEntity.ok(resp)
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @DeleteMapping(value = ["${EndpNames.Bindery.BINDERIES}/{id}"], produces = ["application/json"])
    fun deleteBindery(@PathVariable id: Int) {
        return serv.delete(RecID(id.toLong()))
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @PutMapping(value = ["${EndpNames.Bindery.BINDERIES}/{id}"], produces = ["application/json"])
    fun changeBindery(@PathVariable id: Int, @RequestBody binderyChange: BinderyChangeReq): ResponseEntity<ChangeResp> {
        val changed = serv.change(id, binderyChange)
        return ResponseEntity.of(Optional.ofNullable(changed))
    }
}