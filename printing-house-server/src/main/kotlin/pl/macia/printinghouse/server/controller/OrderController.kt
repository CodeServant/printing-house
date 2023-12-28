package pl.macia.printinghouse.server.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.web.bind.annotation.*
import pl.macia.printinghouse.response.OrderResp
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
}