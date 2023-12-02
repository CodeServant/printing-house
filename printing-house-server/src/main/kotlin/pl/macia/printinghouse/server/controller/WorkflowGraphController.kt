package pl.macia.printinghouse.server.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pl.macia.printinghouse.request.WorkflowGraphReq
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.response.WorkflowGraphResp
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.services.VertexException
import pl.macia.printinghouse.server.services.WorkflowGraphService
import java.util.*

@RestController
@RequestMapping(EndpNames.API_CONTEXT)
@EnableMethodSecurity(prePostEnabled = true)
@Tag(
    name = "workflow graph",
    description = "workflow graph which holds a WorkflowStages template to for typical use cases"
)
@CrossOrigin
class WorkflowGraphController {
    @Autowired
    private lateinit var serv: WorkflowGraphService

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @GetMapping(value = ["${EndpNames.WorkflowDirGraph.WORKFLOW_GRAPHS}/{id}"], produces = ["application/json"])
    fun getWorkflowGraph(@PathVariable id: Int): ResponseEntity<WorkflowGraphResp> {
        val graph: WorkflowGraphResp? = serv.getWholeGraph(id)
        return ResponseEntity.of(Optional.ofNullable(graph))
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}','${PrimaryRoles.SALESMAN}')")
    @PostMapping(value = [EndpNames.WorkflowDirGraph.WORKFLOW_GRAPHS], produces = ["application/json"])
    fun insesrtNew(@RequestBody workflowGraphReq: WorkflowGraphReq): RecID {
        try {
            return serv.insertNewGraph(workflowGraphReq)
        } catch (e: VertexException) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST, e.message, e
            )
        }
    }
}