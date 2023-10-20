package pl.macia.printinghouse.server.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.macia.printinghouse.response.WorkflowStageResp
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.services.WorkflowStageServ
import java.util.*

@RestController
@RequestMapping(EndpNames.API_CONTEXT)
@EnableMethodSecurity(prePostEnabled = true)
@Tag(name = "workflow stage", description = "workflow stage controller")
class WorkflowStageController {
    @Autowired
    private lateinit var serv: WorkflowStageServ

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @GetMapping(value = [EndpNames.WorkflowStage.WORKFLOW_STAGES], produces = ["application/json"])
    fun getAllWorkflowStages(): ResponseEntity<List<WorkflowStageResp>> {
        return ResponseEntity.ok(serv.listWorkflowStages())
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.EMPLOYEE}')")
    @GetMapping(value = ["${EndpNames.WorkflowStage.WORKFLOW_STAGES}/{id}"], produces = ["application/json"])
    fun getWorkflowStage(@PathVariable id: Int): ResponseEntity<WorkflowStageResp> {
        val workflowStage = Optional.ofNullable(serv.findById(id))
        return ResponseEntity.of(workflowStage)
    }
}