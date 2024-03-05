package pl.macia.printinghouse.server.controller

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.web.bind.annotation.*
import pl.macia.printinghouse.roles.PrimaryRoles

@RestController
@RequestMapping(EndpNames.API_CONTEXT)
@EnableMethodSecurity(prePostEnabled = true)
@Tag(
    name = "Task controller",
    description = "WorkflowStageStop is a task that is being conducted for the work to be done"
)
@CrossOrigin
class WorkflowStageStopController {
    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.WORKER}')")
    @PutMapping(
        params = ["wwsIdDone"],
        value = ["${EndpNames.Order.ORDERS}/{orderId}"],
        produces = ["application/json"]
    )
    fun taskDone(
        @PathVariable
        orderId: Int,
        @RequestParam
        @Parameter(
            required = false,
            description = "task id to mark as done"
        )
        wwsIdDone: Int
    ) {
        TODO("mark workflowStageStop as done, creates next wss's if all required tasks are done")
        //todo workflowStageStop should have the date time field done to indicate when wss was done
        // otherwise we can't determine if wss is done other than to check the existence of next wss
        // but this means creating new wss marks as done all previous wss
    }
}