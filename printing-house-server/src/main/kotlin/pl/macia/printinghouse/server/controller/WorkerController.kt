package pl.macia.printinghouse.server.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.macia.printinghouse.response.WorkerResp
import pl.macia.printinghouse.server.services.WorkerService


@RestController
@RequestMapping(WorkerController.CONTEXT)
class WorkerController {
    @Autowired
    private lateinit var serv: WorkerService

    @GetMapping(value = [WORKERS], produces = ["application/json"])
    fun getAllWorkers(): ResponseEntity<List<WorkerResp>> {
        return ResponseEntity.ok(serv.listWorkers())
    }

    @GetMapping(value = ["$WORKERS/{id}"], produces = ["application/json"])
    fun getWorker(@PathVariable id: Int): ResponseEntity<WorkerResp> {
        val worker = serv.findById(id)
        if (worker != null) {
            return ResponseEntity.ok(worker)
        }
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    companion object {
        const val CONTEXT = "api"
        const val WORKERS = "workers"
    }
}