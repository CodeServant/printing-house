package pl.macia.printinghouse.server.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.web.bind.annotation.*
import pl.macia.printinghouse.request.BindingFormChangeReq
import pl.macia.printinghouse.request.BindingFormReq
import pl.macia.printinghouse.response.BindingFormResp
import pl.macia.printinghouse.response.ChangeResp
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.services.BindingFormService
import java.util.*

@RestController
@RequestMapping(EndpNames.API_CONTEXT)
@EnableMethodSecurity(prePostEnabled = true)
@Tag(name = "binding form", description = "binding form controller")
class BindingFormController {
    @Autowired
    private lateinit var serv: BindingFormService

    @Operation(summary = "get all binding forms")
    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}','${PrimaryRoles.SALESMAN}')")
    @GetMapping(value = [EndpNames.BindingForm.BINDING_FORMS], produces = ["application/json"])
    fun getAllBindingForms(): ResponseEntity<List<BindingFormResp>> {
        return ResponseEntity.ok(serv.listAllBindingForms())
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}','${PrimaryRoles.SALESMAN}')")
    @GetMapping(value = ["${EndpNames.BindingForm.BINDING_FORMS}/{id}"], produces = ["application/json"])
    fun findById(@PathVariable id: Int): ResponseEntity<BindingFormResp> {
        val found = Optional.ofNullable(serv.findById(id))
        return ResponseEntity.of(found)
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}','${PrimaryRoles.SALESMAN}')")
    @PostMapping(value = [EndpNames.BindingForm.BINDING_FORMS], produces = ["application/json"])
    fun newBindingForm(@RequestBody req: BindingFormReq): ResponseEntity<RecID> {
        val resp = serv.insertNew(req)
        return ResponseEntity.ok(resp)
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}','${PrimaryRoles.SALESMAN}')")
    @DeleteMapping(value = ["${EndpNames.BindingForm.BINDING_FORMS}/{id}"], produces = ["application/json"])
    fun deleteBindingForm(@PathVariable id: Int) {
        return serv.delete(RecID(id.toLong()))
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}','${PrimaryRoles.SALESMAN}')")
    @PutMapping(value = ["${EndpNames.BindingForm.BINDING_FORMS}/{id}"], produces = ["application/json"])
    fun changeBindingForm(
        @PathVariable id: Int,
        @RequestBody bindFormChange: BindingFormChangeReq
    ): ResponseEntity<ChangeResp> {
        val changed = serv.change(id, bindFormChange)
        return ResponseEntity.of(Optional.ofNullable(changed))
    }
}