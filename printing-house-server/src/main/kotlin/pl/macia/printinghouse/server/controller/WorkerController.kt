package pl.macia.printinghouse.server.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.web.bind.annotation.*
import pl.macia.printinghouse.request.WorkerChangeReq
import pl.macia.printinghouse.request.WorkerReq
import pl.macia.printinghouse.response.ChangeResp
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.response.WorkerResp
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.services.WorkerService
import pl.macia.printinghouse.server.controller.EndpNames.Worker.WORKERS

@RestController
@RequestMapping(EndpNames.API_CONTEXT)
@EnableMethodSecurity(prePostEnabled = true)
class WorkerController {
    @Autowired
    private lateinit var serv: WorkerService

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}', '${PrimaryRoles.WORKFLOW_STAGE_MANAGER}')")
    @GetMapping(value = [WORKERS], produces = ["application/json"])
    fun getAllWorkers(): ResponseEntity<List<WorkerResp>> {
        return ResponseEntity.ok(serv.listWorkers())
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}', '${PrimaryRoles.WORKFLOW_STAGE_MANAGER}')")
    @GetMapping(value = ["${WORKERS}/{id}"], produces = ["application/json"])
    fun getWorker(@PathVariable id: Int): ResponseEntity<WorkerResp> {
        val worker = serv.findById(id)
        if (worker != null) {
            return ResponseEntity.ok(worker)
        }
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @PostMapping(value = [WORKERS], produces = ["application/json"])
    fun newWorker(@RequestBody req: WorkerReq): ResponseEntity<RecID> {
        val resp = serv.insertNew(req)
        return ResponseEntity.ok(resp)
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @DeleteMapping(value = ["$WORKERS/{id}"], produces = ["application/json"])
    fun deleteWorker(@PathVariable id: Int) {
        return serv.delete(RecID(id.toLong()))
    }

    @PreAuthorize("hasAnyAuthority('${PrimaryRoles.MANAGER}')")
    @PutMapping(value = ["$WORKERS/{id}"], produces = ["application/json"])
    fun changeWorker(@PathVariable id: Int, @RequestBody workerChange: WorkerChangeReq): ResponseEntity<ChangeResp> {
        val changed = serv.change(id, workerChange)
        return ResponseEntity.ok(ChangeResp(changed))
    }
}