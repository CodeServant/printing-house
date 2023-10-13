package pl.macia.printinghouse.server.services

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import pl.macia.printinghouse.converting.ConversionException
import pl.macia.printinghouse.request.WorkerReq
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.response.RoleResp
import pl.macia.printinghouse.response.WorkerResp
import pl.macia.printinghouse.response.WorkflowStageRespEmb
import pl.macia.printinghouse.server.bmodel.Email
import pl.macia.printinghouse.server.bmodel.Worker
import pl.macia.printinghouse.server.repository.WorkerRepo

@Service
class WorkerService {
    @Autowired
    private lateinit var repo: WorkerRepo

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    /**
     * Lists all workers that are hired in the Printing House
     */
    fun listWorkers(): List<WorkerResp> {
        return repo.findAll().map { it.toTransport() }
    }

    fun findById(id: Int): WorkerResp? {
        return repo.findById(id)?.toTransport()
    }

    fun insertNew(worker: WorkerReq): RecID {
        val work = repo.save(
            Worker(
                Email(worker.email),
                passwordEncoder.encode(worker.password),
                worker.activeAccount,
                worker.employed,
                worker.name,
                worker.surname,
                worker.psudoPESEL
            )
        )
        return RecID(work.personId!!.toLong())
    }
    @Transactional
    fun delete(id:RecID){
        repo.delete(id.asInt())
    }
}

/**
 * @throws ConversionException
 */
private fun Worker.toTransport(): WorkerResp {
    return WorkerResp(
        id = if (personId == null) throw ConversionException("${this::personId.name} cannot be null") else personId!!,
        isManagerOf.map {
            if (it.workflowStageid == null ||
                it.role.roleId == null
            )
                throw ConversionException()
            WorkflowStageRespEmb(
                it.workflowStageid!!,
                it.name,
                RoleResp(it.role.roleId!!, it.role.name)
            )
        },
        roles.map {
            if (it.roleId == null) throw ConversionException()
            RoleResp(
                it.roleId!!,
                it.name
            )
        },
        employed,
        activeAccount,
        email.email,
        psudoPESEL,
        surname,
        name
    )
}