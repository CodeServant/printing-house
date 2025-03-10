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
import pl.macia.printinghouse.request.WorkflowStageChangeReq
import pl.macia.printinghouse.request.WorkflowStageReq
import pl.macia.printinghouse.response.ChangeResp
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.response.WorkflowStageResp
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.services.WorkflowStageServ
import java.util.*

@RestController
@RequestMapping(EndpNames.API_CONTEXT)
@EnableMethodSecurity(prePostEnabled = true)
@Tag(name = "workflow stage", description = "workflow stage controller")
@CrossOrigin
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

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}','${PrimaryRoles.SALESMAN}')")
    @PostMapping(value = [EndpNames.WorkflowStage.WORKFLOW_STAGES], produces = ["application/json"])
    fun newWorkflowStage(@RequestBody req: WorkflowStageReq): ResponseEntity<RecID> {
        try {
            val resp = serv.insertNew(req)
            return ResponseEntity.ok(resp)
        } catch (e: DataIntegrityViolationException) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST, e.message, e
            )
        }
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @DeleteMapping(value = ["${EndpNames.WorkflowStage.WORKFLOW_STAGES}/{id}"], produces = ["application/json"])
    fun deleteWorkflowStage(@PathVariable id: Int): ResponseEntity<RecID> {
        return ResponseEntity.ok(serv.deleteWithId(id))
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}','${PrimaryRoles.SALESMAN}')")
    @PutMapping(value = ["${EndpNames.WorkflowStage.WORKFLOW_STAGES}/{id}"], produces = ["application/json"])
    fun changeWorkflowStage(
        @PathVariable id: Int,
        @RequestBody changeReq: WorkflowStageChangeReq
    ): ResponseEntity<ChangeResp> {
        val resp = Optional.ofNullable(serv.changeWorkflowStage(id, changeReq))
        return ResponseEntity.of(resp)
    }
}