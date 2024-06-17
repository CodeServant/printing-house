package pl.macia.printinghouse.server.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.transaction.TransactionalException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pl.macia.printinghouse.request.OrderReq
import pl.macia.printinghouse.response.ChangeResp
import pl.macia.printinghouse.response.OrderResp
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.services.OrderService
import java.util.*

@RestController
@RequestMapping(EndpNames.API_CONTEXT)
@EnableMethodSecurity(prePostEnabled = true)
@Tag(name = "Order controller", description = "controller for managing order")
@CrossOrigin
class OrderController {
    @Autowired
    private lateinit var serv: OrderService

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @GetMapping(value = ["${EndpNames.Order.ORDERS}/{id}"], produces = ["application/json"])
    fun findById(@PathVariable id: Int): ResponseEntity<OrderResp> {
        val found = Optional.ofNullable(serv.findById(id))
        return ResponseEntity.of(found)
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}','${PrimaryRoles.SALESMAN}')")
    @PostMapping(value = [EndpNames.Order.ORDERS], produces = ["application/json"])
    fun newOrder(@RequestBody req: OrderReq, authentication: Authentication): ResponseEntity<RecID> {
        if (req.checked && "SALESMAN" in authentication.authorities.map { it.authority }) {
            throw ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "salesman doesn't have permission to mark order as checked"
            )
        }
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
    @Operation(description = "fetches order that are currently assigned for the worker")
    @GetMapping(value = [EndpNames.Order.ORDERS], produces = ["application/json"])
    fun getOrders(
        @Parameter(required = false, description = "fetches order that are currently assigned for the worker")
        @RequestParam(required = false) lastAssignee: Int
    ): ResponseEntity<List<OrderResp>> {
        val ordResp = serv.getOrdersForAssignee(lastAssignee)
        return ResponseEntity.of(Optional.of(ordResp))
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.WORKER}')")
    @GetMapping(params = ["currentWorker"], value = [EndpNames.Order.ORDERS], produces = ["application/json"])
    fun getAUthWorkerOrders(
        @Parameter(
            required = false,
            description = "fetches order that are currently assigned for the currently authenticated worker, doesn't matter if true or false"
        )
        @RequestParam(required = false) currentWorker: Boolean, authentication: Authentication
    ): ResponseEntity<List<OrderResp>> {
        val ordResp = serv.getOrdersForAssignee(authentication)
        return ResponseEntity.of(Optional.of(ordResp))
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.WORKFLOW_STAGE_MANAGER}')")
    @GetMapping(params = ["notAssigned"], value = [EndpNames.Order.ORDERS], produces = ["application/json"])
    fun getUnasignedOrders(
        @Parameter(
            required = false,
            description = "fetches orders that are not assigned wor workflow stege of the manager"
        )
        @RequestParam(required = false) notAssigned: Boolean, authentication: Authentication
    ): ResponseEntity<List<OrderResp>> {
        return ResponseEntity.ok(serv.fetchNotAssigned(authentication.name))
    } //todo test this controller

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.WORKFLOW_STAGE_MANAGER}')")
    @PutMapping(value = ["${EndpNames.Order.ORDERS}/{id}"], produces = ["application/json"])
    fun assignWorker(
        @PathVariable id: Int,
        @RequestParam(required = false) workerId: Int,
        authentication: Authentication
    ): ResponseEntity<*> {
        return ResponseEntity.ok(serv.assignWorker(id, workerId, authentication))
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.SALESMAN}')")
    @GetMapping(value = [EndpNames.Order.ORDERS], produces = ["application/json"], params = ["toFinalize"])
    fun ordersToFinalize(
        @Parameter(description = "orders that are not finalized for the currently authenticated salesman")
        @RequestParam(required = false, defaultValue = "true")
        toFinalize: Boolean,
        authentication: Authentication
    ): ResponseEntity<List<OrderResp>> {
        return ResponseEntity.ok(serv.ordersToFinalize(authentication.name))
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.SALESMAN}')")
    @PutMapping(value = ["${EndpNames.Order.ORDERS}/{id}"], produces = ["application/json"], params = ["finalize"])
    fun finalizeOrder(
        @PathVariable
        id: Int,
        @RequestParam(required = false, defaultValue = "true")
        finalize: Boolean
    ): ResponseEntity<Void> {
        if (serv.finalizeOrder(id))
            return ResponseEntity.noContent().build()
        return ResponseEntity.badRequest().build()
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @GetMapping(value = [EndpNames.Order.ORDERS], produces = ["application/json"], params = ["toCheck"])
    fun ordersToCheck(
        @Parameter(description = "orders that are not yet accepted")
        @RequestParam(required = false)
        toCheck: Boolean
    ): ResponseEntity<List<OrderResp>> {
        return ResponseEntity.ok(serv.ordersToCheck())
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @PutMapping(value = ["${EndpNames.Order.ORDERS}/{id}"], produces = ["application/json"], params = ["checked"])
    fun markOrderAsChecked(
        @PathVariable id: Int,
        @RequestParam(required = false, defaultValue = "true")
        checked: Boolean
    ): ResponseEntity<ChangeResp> {
        val ifChanged = serv.markOrderAsChecked(id)
        return ResponseEntity.ok(ChangeResp(ifChanged))
    }
}