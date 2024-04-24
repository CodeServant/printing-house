package pl.macia.printinghouse.server.services

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import pl.macia.printinghouse.converting.ConversionException
import pl.macia.printinghouse.request.WorkerChangeReq
import pl.macia.printinghouse.request.WorkerReq
import pl.macia.printinghouse.response.RecID
import pl.macia.printinghouse.response.RoleResp
import pl.macia.printinghouse.response.WorkerResp
import pl.macia.printinghouse.response.WorkflowStageRespEmb
import pl.macia.printinghouse.roles.PrimaryRoles
import pl.macia.printinghouse.server.bmodel.*
import pl.macia.printinghouse.server.repository.RoleRepo
import pl.macia.printinghouse.server.repository.WorkerRepo
import pl.macia.printinghouse.server.repository.WorkflowStageRepo

@Service
class WorkerService {
    @Autowired
    private lateinit var repo: WorkerRepo

    @Autowired
    private lateinit var empServ: EmployeeService

    @Autowired
    private lateinit var workflowStageRepo: WorkflowStageRepo

    @Autowired
    private lateinit var roleRepo: RoleRepo

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    /**
     * Lists all workers that are hired in the Printing House
     */
    fun listWorkers(): List<WorkerResp> {
        return repo.findAll().map { it.toTransport() } //todo change to hired workers
    }

    fun findById(id: Int): WorkerResp? {
        return repo.findById(id)?.toTransport()
    }

    /**
     * Sets workflow stages that this is manager of. Finds it by id and sets it to the worker.
     */
    private fun Worker.setIsManagerOf(managerOf: List<Int>) {
        this.isManagerOf.addAll(
            workflowStageRepo.findAllById(managerOf)

        )
        this.isManagerOf.forEach {
            it.workflowManagers.add(this)
        }
        if (this.isManagerOf.isNotEmpty())
            this.onRoleWorkflowManager()
        else
            this.offRoleWorkflowManager()
    }

    @Transactional
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
        work.setRoleWorker()
        work.setIsManagerOf(worker.isManagerOf)
        empServ.justInserted(work)
        return RecID(work.personId!!.toLong())
    }

    @Transactional
    fun delete(id: RecID) {
        repo.deleteById(id.asInt())
    }

    /**
     * @return true if changed
     */
    @Transactional
    fun change(id: Int, workerChange: WorkerChangeReq): Boolean {
        var workerChanged = false
        val found = repo.findById(id) ?: return false
        workerChanged = workerChanged || empServ.change(found, workerChange)

        if (workerChange.isManagerOf != null &&
            found.isManagerOf.map { it.workflowStageid }.toSet() != workerChange.isManagerOf!!.toSet()
        ) {
            found.isManagerOf.forEach {
                it.workflowManagers.remove(found)
            }
            found.isManagerOf.clear()
            found.setIsManagerOf(workerChange.isManagerOf!!)
            workerChanged = true
        } else if (workerChange.nullingRest) {
            found.isManagerOf.clear()
            found.setIsManagerOf(listOf())
            workerChanged = true
        }


        return workerChanged
    }

    private fun Worker.onRoleWorkflowManager() {
        roles.add(roleRepo.mergeName(PrimaryRoles.WORKFLOW_STAGE_MANAGER))
    }

    private fun Worker.offRoleWorkflowManager() {
        roles.removeIf { it.name == PrimaryRoles.WORKFLOW_STAGE_MANAGER }
    }

    private fun Worker.setRoleWorker() {
        roles.add(roleRepo.mergeName(PrimaryRoles.WORKER))
    }

    fun getByEmail(email: String): WorkerResp? {
        return repo.findByEmail(email)?.toTransport()
    }

    fun searchWithQuery(query: String): List<WorkerResp> {
        return repo.searchQuery(query).map { it.toTransport() }
    }
}

/**
 * @throws ConversionException
 */
internal fun Worker.toTransport(): WorkerResp {
    return WorkerResp(
        id = if (personId == null) throw ConversionException("${this::personId.name} cannot be null") else personId!!,
        isManagerOf.map {
            if (it.workflowStageid == null)
                throw ConversionException()
            WorkflowStageRespEmb(
                it.workflowStageid!!,
                it.name
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