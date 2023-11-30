package pl.macia.printinghouse.server.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.transaction.TransactionalException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pl.macia.printinghouse.request.IEnoblingChangeReq
import pl.macia.printinghouse.request.IEnoblingReq
import pl.macia.printinghouse.response.ChangeResp
import pl.macia.printinghouse.response.EnoblingResp
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.services.EnoblingService
import pl.macia.printinghouse.server.services.changeExceptionCatch
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

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}','${PrimaryRoles.SALESMAN}')")
    @GetMapping(value = [EndpNames.Enobling.ENOBLINGS], produces = ["application/json"])
    fun getAllEnoblings(): ResponseEntity<List<EnoblingResp>> {
        return ResponseEntity.ok(serv.allEnoblings())
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @PostMapping(value = [EndpNames.Enobling.ENOBLINGS], produces = ["application/json"])
    fun newEnobling(@RequestBody req: IEnoblingReq): ResponseEntity<RecID> {
        try {
            val resp = serv.insertNew(req)
            return ResponseEntity.ok(resp)
        } catch (e: TransactionalException) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                e.message,
                e
            )
        }
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @PutMapping(value = ["${EndpNames.Enobling.ENOBLINGS}/{id}"], produces = ["application/json"])
    fun changeEnobling(@PathVariable id: Int, @RequestBody req: IEnoblingChangeReq): ResponseEntity<ChangeResp> {
        return changeExceptionCatch {
            val changeResp: Optional<ChangeResp> = Optional.ofNullable(serv.changeEnobling(id = id, changeReq = req))
            ResponseEntity.of(changeResp)
        }
    }
}