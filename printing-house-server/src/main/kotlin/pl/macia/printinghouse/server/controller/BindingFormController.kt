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
import pl.macia.printinghouse.response.BindingFormResp
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.services.BindingFormService

@RestController
@RequestMapping(EndpNames.API_CONTEXT)
@EnableMethodSecurity(prePostEnabled = true)
@Tag(name = "BindingForm", description = "binding form controller")
class BindingFormController {
    @Autowired
    private lateinit var serv: BindingFormService

    @Operation(summary = "get all binding forms")
    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}','${PrimaryRoles.SALESMAN}')")
    @GetMapping(value = [EndpNames.BindingForm.BINDING_FORMS], produces = ["application/json"])
    fun getAllBindingForms(): ResponseEntity<List<BindingFormResp>> {
        return ResponseEntity.ok(serv.listAllBindingForms())
    }
}