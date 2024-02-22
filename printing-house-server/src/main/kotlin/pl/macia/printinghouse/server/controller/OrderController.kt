package pl.macia.printinghouse.server.controller

import io.swagger.v3.oas.annotations.Operation
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

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @PostMapping(value = [EndpNames.Order.ORDERS], produces = ["application/json"])
    fun newOrder(@RequestBody req: OrderReq): ResponseEntity<RecID> {
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

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.WORKER}')")
    @Operation(description = "fetches order with such workflow stage stops that the last one has the provided worker as a assignee")
    @GetMapping(value = [EndpNames.Order.ORDERS], produces = ["application/json"])
    fun getOrders(@RequestParam lastAssignee: Int, authentication: Authentication): ResponseEntity<List<OrderResp>> {
        val ordResp = serv.getOrdersForAssignee(lastAssignee, authentication)
        return ResponseEntity.of(Optional.of(ordResp))
    }
}