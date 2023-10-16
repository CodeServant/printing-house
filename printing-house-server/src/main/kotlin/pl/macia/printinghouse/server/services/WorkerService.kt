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
import pl.macia.printinghouse.server.bmodel.*
import pl.macia.printinghouse.server.repository.RoleRepo
import pl.macia.printinghouse.server.repository.WorkerRepo
import pl.macia.printinghouse.server.repository.WorkflowStageRepo

@Service
class WorkerService {
    @Autowired
    private lateinit var repo: WorkerRepo

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
    fun delete(id: RecID) {
        repo.delete(id.asInt())
    }

    /**
     * @return true if changed
     */
    @Transactional
    fun change(id: Int, workerChange: WorkerChangeReq): Boolean {
        var workerChanged = false
        val found = repo.findById(id) ?: return false
        if (workerChange.isManagerOf != null &&
            found.isManagerOf.map { it.workflowStageid }.toSet() != workerChange.isManagerOf!!.toSet()
        ) {
            found.isManagerOf.forEach {
                it.workflowManagers.remove(found)
            }
            found.isManagerOf.clear()
            found.isManagerOf.addAll(
                workflowStageRepo.findAllById(workerChange.isManagerOf!!)

            )
            found.isManagerOf.forEach {
                it.workflowManagers.add(found)
            }
            workerChanged = true
        } else if (workerChange.nullingRest) {
            found.isManagerOf.clear()
            workerChanged = true
        }


        if (workerChange.roles != null &&
            found.roles.map { it.roleId }.toSet() != workerChange.roles!!.toSet()
        ) {
            found.roles.clear()
            found.roles.addAll(
                roleRepo.findAllById(workerChange.roles!!)
            )
            workerChanged = true
        } else if (workerChange.nullingRest) {
            found.roles.clear()
            workerChanged = true
        }

        fun <E> simpleChange(workerChange: E, found: E, setFound: (E) -> Unit) {
            workerChange?.let {
                if (found != it) {
                    setFound(it)
                    workerChanged = true
                }
            }
        }
        simpleChange(workerChange.employed, found.employed) { found.employed = it!! }
        simpleChange(workerChange.activeAccount, found.activeAccount) { found.activeAccount = it!! }
        workerChange.password?.let {
            //password changes allways if not null because of salt
            found.password = passwordEncoder.encode(it)
            workerChanged = true
        }
        simpleChange(workerChange.email, found.email.email) { found.email.email = it!! }
        simpleChange(workerChange.psudoPESEL, found.psudoPESEL) { found.psudoPESEL = it!! }
        simpleChange(workerChange.surname, found.surname) { found.surname = it!! }
        simpleChange(workerChange.name, found.name) { found.name = it!! }
        found.psudoPESEL = found.psudoPESEL.padEnd(11, ' ') //todo delete this
        return workerChanged
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