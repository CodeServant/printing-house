package pl.macia.printinghouse.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import pl.macia.printinghouse.server.dto.Email as PEmail
import pl.macia.printinghouse.server.bmodel.Worker
import pl.macia.printinghouse.server.dto.Worker as PWorker
import pl.macia.printinghouse.server.bmodel.WorkerImpl
import pl.macia.printinghouse.server.dao.WorkerDAO

@Repository
internal class WorkerRepoImpl : WorkerIntRepo {
    @Autowired
    lateinit var dao: WorkerDAO
    private fun PWorker.toBiz(): WorkerImpl = WorkerImpl(this)
    override fun save(obj: Worker): Worker {
        obj as WorkerImpl
        return dao.save(obj.persistent).toBiz()
    }

    override fun findById(id: Int): Worker? {
        return dao.findByIdOrNull(id)?.toBiz()
    }

    override fun findAll(): List<Worker> {
        return dao.findAll().map { it.toBiz() }
    }

    override fun deleteById(id: Int) {
        dao.deleteById(id)
    }

    override fun findAllById(ids: Iterable<Int>): List<Worker> {
        return dao.findAllById(ids).map { it.toBiz() }
    }

    override fun findByEmail(email: String): Worker? {
        return dao.findByEmail(PEmail(email))?.toBiz()
    }
}