package pl.macia.printinghouse.server.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.macia.printinghouse.converting.ConvertionException
import pl.macia.printinghouse.response.RoleResp
import pl.macia.printinghouse.response.WorkerResp
import pl.macia.printinghouse.response.WorkflowStageRespEmb
import pl.macia.printinghouse.server.bmodel.Worker
import pl.macia.printinghouse.server.repository.WorkerRepo

@Service
class WorkerService {
    @Autowired
    private lateinit var repo: WorkerRepo

    /**
     * Lists all workers that are hired in the Printing House
     */
    fun listWorkers(): List<WorkerResp> {
        return repo.findAll().map { it.toTransport() }
    }
}

private fun Worker.toTransport(): WorkerResp {
    return WorkerResp(
        isManagerOf.map {
            if (it.workflowStageid == null ||
                it.role.roleId == null
            )
                throw ConvertionException()
            WorkflowStageRespEmb(
                it.workflowStageid!!,
                it.name,
                RoleResp(it.role.roleId!!, it.role.name)
            )
        },
        roles.map {
            if (it.roleId == null) throw ConvertionException()
            RoleResp(
                it.roleId!!,
                it.name
            )
        },
        employed,
        activeAccount,
        password,
        email.email,
        psudoPESEL,
        surname,
        name
    )
}