package pl.macia.printinghouse.server.controller

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.repository.ObjectNotFoundException
import pl.macia.printinghouse.server.services.OrderService

@RestController
@RequestMapping(EndpNames.API_CONTEXT)
@EnableMethodSecurity(prePostEnabled = true)
@Tag(
    name = "Task controller",
    description = "WorkflowStageStop is a task that is being conducted for the work to be done"
)
@CrossOrigin
class WorkflowStageStopController {
    @Autowired
    private lateinit var serv: OrderService

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.WORKER}')")
    @PutMapping(
        params = ["wwsDone"],
        value = ["${EndpNames.WorkflowStageStop.WORKFLOW_STAGE_STOP}/{wssId}"],
        produces = ["application/json"]
    )
    fun taskDone(
        @PathVariable
        wssId: Int,
        @RequestParam
        @Parameter(
            required = false,
            description = "mark task as done"
        )
        wwsDone: Boolean,
        authentication: Authentication
    ): ResponseEntity<Unit> {
        try {
            if (wwsDone)
                serv.markTaskAsDone(wssId, authentication)
            return ResponseEntity.noContent().build()
        } catch (e: ObjectNotFoundException) {
            return ResponseEntity.notFound().build()
        }
    }
}