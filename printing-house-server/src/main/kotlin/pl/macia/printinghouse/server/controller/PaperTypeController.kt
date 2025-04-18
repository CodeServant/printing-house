package pl.macia.printinghouse.server.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pl.macia.printinghouse.request.PaperTypeChangeReq
import pl.macia.printinghouse.request.PaperTypeReq
import pl.macia.printinghouse.response.ChangeResp
import pl.macia.printinghouse.response.PaperTypeResp
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.services.PaperTypeService
import pl.macia.printinghouse.server.services.changeExceptionCatch
import java.sql.SQLException
import java.util.*

@RestController
@RequestMapping(EndpNames.API_CONTEXT)
@EnableMethodSecurity(prePostEnabled = true)
@Tag(name = "paper type", description = "controller for managing paper types")
@CrossOrigin
class PaperTypeController {
    @Autowired
    private lateinit var serv: PaperTypeService

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}','${PrimaryRoles.SALESMAN}')")
    @GetMapping(value = ["${EndpNames.PaperType.PAPER_TYPES}/{id}"], produces = ["application/json"])
    fun findById(@PathVariable id: Int): ResponseEntity<PaperTypeResp> {
        val found = Optional.ofNullable(serv.findById(id))
        return ResponseEntity.of(found)
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}','${PrimaryRoles.SALESMAN}')")
    @GetMapping(value = [EndpNames.PaperType.PAPER_TYPES], produces = ["application/json"])
    fun getAllPapTypes(): ResponseEntity<List<PaperTypeResp>> {
        return ResponseEntity.ok(serv.allPapTypes())
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}','${PrimaryRoles.SALESMAN}')")
    @PostMapping(value = [EndpNames.PaperType.PAPER_TYPES], produces = ["application/json"])
    fun insertOnePapType(@RequestBody papTypeReq: PaperTypeReq): ResponseEntity<RecID> {
        try {
            val resp = serv.insertNew(papTypeReq)
            return ResponseEntity.ok(resp)
        } catch (e: DataIntegrityViolationException) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "possible data duplication",
                e
            )
        } catch (e: SQLException) {
            throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "sql exception while processing request",
                e
            )
        }
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @PutMapping(value = ["${EndpNames.PaperType.PAPER_TYPES}/{id}"], produces = ["application/json"])
    fun changePaperType(@PathVariable id: Int, @RequestBody req: PaperTypeChangeReq): ResponseEntity<ChangeResp> {
        return changeExceptionCatch {
            val changeResp: Optional<ChangeResp> = Optional.ofNullable(serv.changePaperType(id = id, changeReq = req))
            ResponseEntity.of(changeResp)
        }
    }
}