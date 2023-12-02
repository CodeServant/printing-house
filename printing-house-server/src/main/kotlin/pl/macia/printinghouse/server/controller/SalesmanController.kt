package pl.macia.printinghouse.server.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.web.bind.annotation.*
import pl.macia.printinghouse.request.SalesmanChangeReq
import pl.macia.printinghouse.request.SalesmanReq
import pl.macia.printinghouse.response.ChangeResp
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.response.SalesmanResp
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.services.SalesmanService
import java.util.*

@RestController
@RequestMapping(EndpNames.API_CONTEXT)
@EnableMethodSecurity(prePostEnabled = true)
@Tag(name = "salesman", description = "salesman controller")
@CrossOrigin
class SalesmanController {
    @Autowired
    private lateinit var serv: SalesmanService

    @Operation(summary = "get all hired salesmans")
    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @GetMapping(value = [EndpNames.Salesman.SALESMANS], produces = ["application/json"])
    fun getHiredSalesmans(): ResponseEntity<List<SalesmanResp>> {
        return ResponseEntity.ok(serv.listSalesmans())
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @GetMapping(value = ["${EndpNames.Salesman.SALESMANS}/{id}"], produces = ["application/json"])
    fun findById(@PathVariable id: Int): ResponseEntity<SalesmanResp> {
        val found = Optional.ofNullable(serv.findById(id))
        return ResponseEntity.of(found)
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @PostMapping(value = [EndpNames.Salesman.SALESMANS], produces = ["application/json"])
    fun newSalesman(@RequestBody req: SalesmanReq): ResponseEntity<RecID> {
        val resp = serv.insertNew(req)
        return ResponseEntity.ok(resp)
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @DeleteMapping(value = ["${EndpNames.Salesman.SALESMANS}/{id}"], produces = ["application/json"])
    fun deleteSalesman(@PathVariable id: Int) {
        return serv.delete(RecID(id.toLong()))
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @PutMapping(value = ["${EndpNames.Salesman.SALESMANS}/{id}"], produces = ["application/json"])
    fun changeSalesman(
        @PathVariable id: Int,
        @RequestBody salesmanChange: SalesmanChangeReq
    ): ResponseEntity<ChangeResp> {
        val changed = serv.change(id, salesmanChange)
        return ResponseEntity.ok(ChangeResp(changed))
    }
}