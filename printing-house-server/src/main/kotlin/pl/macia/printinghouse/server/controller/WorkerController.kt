package pl.macia.printinghouse.server.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.macia.printinghouse.response.WorkerResp
import pl.macia.printinghouse.server.services.WorkerService


@RestController
@RequestMapping("api")
class WorkerController {
    @Autowired
    private lateinit var serv: WorkerService

    @GetMapping(value = ["/${WORKERS}/"], produces = ["application/json"])
    fun getAllWorkers(): ResponseEntity<List<WorkerResp>> {
        return ResponseEntity.ok(serv.listWorkers())
    }

    companion object {
        const val WORKERS = "workers"
    }
}