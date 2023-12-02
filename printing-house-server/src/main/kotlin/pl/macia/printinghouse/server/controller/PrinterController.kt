package pl.macia.printinghouse.server.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.web.bind.annotation.*
import pl.macia.printinghouse.request.PrinterChangeReq
import pl.macia.printinghouse.request.PrinterReq
import pl.macia.printinghouse.response.ChangeResp
import pl.macia.printinghouse.response.PrinterResp
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.services.PrinterService
import pl.macia.printinghouse.server.services.changeExceptionCatch
import java.util.*

@RestController
@RequestMapping(EndpNames.API_CONTEXT)
@EnableMethodSecurity(prePostEnabled = true)
@Tag(name = "printer", description = "controller for managing printers")
@CrossOrigin
class PrinterController {
    @Autowired
    private lateinit var serv: PrinterService

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @GetMapping(value = ["${EndpNames.Printer.PRINTERS}/{id}"], produces = ["application/json"])
    fun findById(@PathVariable id: Int): ResponseEntity<PrinterResp> {
        val found = Optional.ofNullable(serv.findById(id))
        return ResponseEntity.of(found)
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @GetMapping(value = [EndpNames.Printer.PRINTERS], produces = ["application/json"])
    fun getAllPrinters(): ResponseEntity<List<PrinterResp>> {
        return ResponseEntity.ok(serv.allPrinters())
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @PostMapping(value = [EndpNames.Printer.PRINTERS], produces = ["application/json"])
    fun newPrinter(@RequestBody req: PrinterReq): ResponseEntity<RecID> {
        val resp = serv.insertNew(req)
        return ResponseEntity.ok(resp)
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @PutMapping(value = ["${EndpNames.Printer.PRINTERS}/{id}"], produces = ["application/json"])
    fun changePrinter(@PathVariable id: Int, @RequestBody req: PrinterChangeReq): ResponseEntity<ChangeResp> {
        return changeExceptionCatch {
            val changeResp: Optional<ChangeResp> = Optional.ofNullable(serv.changePrinter(id = id, changeReq = req))
            ResponseEntity.of(changeResp)
        }
    }
}