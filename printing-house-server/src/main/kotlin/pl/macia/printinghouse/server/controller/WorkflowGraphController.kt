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
import pl.macia.printinghouse.response.WorkflowGraphResp
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.services.WorkflowGraphService
import java.util.*

@RestController
@RequestMapping(EndpNames.API_CONTEXT)
@EnableMethodSecurity(prePostEnabled = true)
@Tag(
    name = "workflow graph",
    description = "workflow graph which holds a WorkflowStages template to for typical use cases"
)
class WorkflowGraphController {
    @Autowired
    private lateinit var serv: WorkflowGraphService

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @GetMapping(value = ["${EndpNames.WorkflowDirGraph.WORKFLOW_GRAPHS}/{id}"], produces = ["application/json"])
    fun getWorkflowGraph(@PathVariable id: Int): ResponseEntity<WorkflowGraphResp> {
        val graph: WorkflowGraphResp? = serv.getWholeGraph(id)
        return ResponseEntity.of(Optional.ofNullable(graph))
    }
}